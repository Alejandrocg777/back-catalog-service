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
                numerationRepo.setAuthNumber(numerationDto.getAuthNumber());
                numerationRepo.setPrefix(numerationDto.getPrefix());
                numerationRepo.setStartDate(numerationDto.getStartDate());
                numerationRepo.setFinishDate(numerationDto.getFinishDate());
                numerationRepo.setStatus("ACTIVE");
                numerationRepo.setInitialNumber(Integer.valueOf(numerationDto.getInitialNumber()));
                numerationRepo.setFinalNumber(Integer.valueOf(numerationDto.getFinalNumber()));
                numerationRepo.setCurrentNumber(Integer.valueOf(numerationDto.getCurrentNumber()));

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
            numeration.setAuthNumber(numerationDto.getAuthNumber());
            numeration.setPrefix(numerationDto.getPrefix());
            numeration.setStartDate(numerationDto.getStartDate());
            numeration.setFinishDate(numerationDto.getFinishDate());
            numeration.setStatus(numerationDto.getStatus());
            numeration.setInitialNumber(Integer.valueOf(numerationDto.getInitialNumber()));
            numeration.setFinalNumber(Integer.valueOf(numerationDto.getFinalNumber()));
            numeration.setCurrentNumber(Integer.valueOf(numerationDto.getCurrentNumber()));

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
        String authNumber = null;
        String prefix = null;
        String status = null;
        String initialNumber = null;
        String finalNumber = null;
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

        if (customQuery.containsKey("authNumber")) {
            authNumber = "%" + customQuery.get("authNumber") + "%";
        }

        if (customQuery.containsKey("prefix")) {
            prefix = "%" + customQuery.get("prefix") + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        if (customQuery.containsKey("initialNumber")) {
            initialNumber = "%" + customQuery.get("initialNumber") + "%";
        }

        if (customQuery.containsKey("finalNumber")) {
            finalNumber = "%" + customQuery.get("finalNumber") + "%";
        }

        if (customQuery.containsKey("currentNumber")) {
            currentNumber = "%" + customQuery.get("currentNumber") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);


        Page<NumerationResponseDto> searchResult = repository.searchNumeration(
                id,
                authNumber,
                prefix,
                initialNumber,
                finalNumber,
                currentNumber,
                pagingSort
        );

        log.info("Search results found: " + searchResult.getTotalElements() + " records");
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


    @Transactional
    public String generateInvoiceNumber(Long userId) {
        LocalDate currentDate = LocalDate.now();

        // Encontrar la numeración vigente para el usuario específico con lock pesimista
        Optional<Numeration> optionalNumeration = repository.findActiveNumerationForUser(userId, currentDate);

        if (optionalNumeration.isEmpty()) {
            throw new RuntimeException("No hay numeración vigente asignada al usuario " + userId +
                    " en la fecha actual. Verifica que el usuario tenga un terminal asignado con numeración activa.");
        }

        Numeration numeration = optionalNumeration.get();

        // Validar que no se haya alcanzado el límite
        if (numeration.getCurrentNumber() >= numeration.getFinalNumber()) {
            throw new RuntimeException("Se ha alcanzado el número final en la numeración ID: " +
                    numeration.getId() + " (Prefijo: " + numeration.getPrefix() + ")");
        }

        // Generar el número de factura
        int current = numeration.getCurrentNumber();
        String invoiceNumber = numeration.getPrefix() + "-" + String.format("%06d", current);

        // Incrementar el número actual y guardar
        numeration.setCurrentNumber(current + 1);
        repository.save(numeration);

        log.info("Factura generada: {} para usuario: {} desde numeración ID: {}",
                invoiceNumber, userId, numeration.getId());

        return invoiceNumber;
    }
}
