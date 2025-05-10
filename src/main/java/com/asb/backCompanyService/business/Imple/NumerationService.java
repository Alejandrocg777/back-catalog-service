package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.INumerationBusiness;
import com.asb.backCompanyService.dto.request.NumerationDto;
import com.asb.backCompanyService.dto.responde.NumerationResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Numeration;
import com.asb.backCompanyService.repository.NumerationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class NumerationService implements INumerationBusiness {

    private final NumerationRepository repository;

    @Override
    @Transactional
    public NumerationDto save(NumerationDto numerationDto) {
        try {
            Boolean objectExists = false;
            if (numerationDto.getId() != null) {
                objectExists = repository.existsById(numerationDto.getId());
            }

            NumerationDto objectDtoVo = new NumerationDto();
            if (!objectExists) {
                Numeration numerationRepo = new Numeration();
                numerationRepo.setAccountingDocumentTypeId(numerationDto.getAccountingDocumentTypeId());
                numerationRepo.setAuthNumer(creationResolutionNumber(numerationDto.getStartDate(), numerationDto.getPrefix(), numerationDto.getCurrentNumber()));
                numerationRepo.setPrefix(numerationDto.getPrefix());
                numerationRepo.setStartDate(numerationDto.getStartDate());
                numerationRepo.setFinishDate(numerationDto.getFinishDate());
                numerationRepo.setStatus("ACTIVE");
                numerationRepo.setInitialNumber(Integer.valueOf(numerationDto.getInitialNumber()));
                numerationRepo.setFinalNumber(Integer.valueOf(numerationDto.getFinalNumber()));
                numerationRepo.setCurrentNumber(Integer.valueOf(numerationDto.getCurrentNumber()));
                numerationRepo.setTechnicalKey(numerationDto.getTechnicalKey());

                Numeration newObject = repository.save(numerationRepo);

                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La numeración ya existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la numeración", e);
        }
    }

    private String creationResolutionNumber(LocalDate resolutionDate, String prefix, Integer consecutive){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formatedDate = resolutionDate.format(format);
        return formatedDate + "-" + prefix + "-" + consecutive.toString();
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, NumerationDto numerationDto) {
        GenericResponse response = new GenericResponse();
        try {
            Optional<Numeration> optionalNumeration = repository.findById(id);
            if (!optionalNumeration.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La numeración no existe");
            }

            Numeration numeration = optionalNumeration.get();
            BeanUtils.copyProperties(numerationDto, numeration);
            numeration.setAccountingDocumentTypeId(numerationDto.getAccountingDocumentTypeId());
            numeration.setAuthNumer(numerationDto.getAuthNumer());
            numeration.setPrefix(numerationDto.getPrefix());
            numeration.setStartDate(numerationDto.getStartDate());
            numeration.setFinishDate(numerationDto.getFinishDate());
            numeration.setStatus(numerationDto.getStatus());
            numeration.setInitialNumber(Integer.valueOf(numerationDto.getInitialNumber()));
            numeration.setFinalNumber(Integer.valueOf(numerationDto.getFinalNumber()));
            numeration.setCurrentNumber(Integer.valueOf(numerationDto.getCurrentNumber()));
            numeration.setTechnicalKey(numerationDto.getTechnicalKey());

            repository.save(numeration);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Numeración actualizada");
        } catch (Exception e) {
            log.error("Error al actualizar la numeración: {}", e.getMessage());
            throw new RuntimeException("Error", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            Numeration numeration = repository.findById(id).get();
            numeration.setStatus("INACTIVE");
            repository.save(numeration);
            return true;
        } else {
            throw new RuntimeException("La numeración no fue encontrada por el id " + id);
        }
    }

    @Override
    public NumerationDto get(Long id) {
        Optional<Numeration> numerationOptional = repository.findById(id);
        NumerationDto numerationDto = null;
        if (numerationOptional.isPresent()) {
            numerationDto = new NumerationDto();
            BeanUtils.copyProperties(numerationOptional.get(), numerationDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La numeración no existe");
        }
        return numerationDto;
    }

    @Override
    public Page<NumerationResponseDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getStatus(pagingSort);
    }

    @Override
    public Page<NumerationResponseDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String authNumer = null;
        String prefix = null;
        String status = null;
        String technicalKey = null;
        String descriptionAccountingDocumentType = null;
        String currentNumber = null;


        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }

        if (customQuery.containsKey("authNumer")) {
            authNumer = "%" + customQuery.get("authNumer") + "%";
        }

        if (customQuery.containsKey("prefix")) {
            prefix = "%" + customQuery.get("prefix") + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        if (customQuery.containsKey("technicalKey")) {
            technicalKey = "%" + customQuery.get("technicalKey") + "%";
        }

        if (customQuery.containsKey("descriptionAccountingDocumentType")) {
            descriptionAccountingDocumentType = "%" + customQuery.get("descriptionAccountingDocumentType") + "%";
        }

        if (customQuery.containsKey("currentNumber")) {
            currentNumber = "%" + customQuery.get("currentNumber").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("ID: " + id);
        log.info("Auth Number: " + authNumer);
        log.info("Prefix: " + prefix);
        log.info("Status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<NumerationResponseDto> searchResult = repository.searchNumeration(id, authNumer, prefix, status,technicalKey,descriptionAccountingDocumentType,currentNumber, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<NumerationResponseDto> getAllNumeration() {
        try {
            return repository.getAllNumeration();
        } catch (Exception e) {
            log.error("Error al obtener la numeración");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar la numeración", e);
        }
    }
}
