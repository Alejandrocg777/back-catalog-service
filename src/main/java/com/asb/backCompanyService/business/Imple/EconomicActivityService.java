package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IEconomicActivityBusiness;
import com.asb.backCompanyService.dto.request.EconomicActivityDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ListGenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.EconomicActivity;
import com.asb.backCompanyService.repository.EconomicActivityRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class EconomicActivityService implements IEconomicActivityBusiness {

    private final EconomicActivityRepository repository;

    @Override
    @Transactional
    public EconomicActivityDto save(EconomicActivityDto economicActivityDto) {
        try {
            Boolean objectExists = false;
            if (economicActivityDto.getId() != null) {
                objectExists = repository.existsById(economicActivityDto.getId());
            }
            EconomicActivityDto objectDtoVo = new EconomicActivityDto();
            if (!objectExists) {
                EconomicActivity economicActivityRepo = new EconomicActivity();
                economicActivityRepo.setCiiuCode(Long.valueOf(economicActivityDto.getCiiuCode().toString()));
                economicActivityRepo.setDescription(economicActivityDto.getDescription());
                economicActivityRepo.setStatus("ACTIVE");

                EconomicActivity newObject = repository.save(economicActivityRepo);

                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La actividad economica ya existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar al actividad economica", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(long id, EconomicActivityDto economicActivityDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando método de actualización para CiiuCode con ID: {} y CiiuCodeDto: {}", id, economicActivityDto);

            Optional<EconomicActivity> optionalEconomicActivity = repository.findById(id);
            if (!optionalEconomicActivity.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El código CIIU no existe");
            }

            EconomicActivity economicActivity = optionalEconomicActivity.get();
            economicActivity.setCiiuCode(Long.valueOf(economicActivityDto.getCiiuCode().toString()));
            economicActivity.setDescription(economicActivityDto.getDescription());

            repository.save(economicActivity);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Código CIIU actualizado");
        } catch (Exception e) {
            log.error("Se produjo un error al actualizar el código CIIU: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar el código CIIU", e);
        }
        return response;
    }


    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            EconomicActivity ciiuCode = repository.findById(id).get();
            ciiuCode.setStatus("INACTIVE");
            repository.save(ciiuCode);
            return true;
        } else {
            throw new RuntimeException("El código CIIU no fue encontrado por el id " + id);
        }
    }

    @Override
    public EconomicActivityDto get(long id) {
        Optional<EconomicActivity> ciiuCodeOptional = repository.findById(id);
        EconomicActivityDto ciiuCodeDto = null;
        if (ciiuCodeOptional.isPresent()) {
            ciiuCodeDto = new EconomicActivityDto();
            BeanUtils.copyProperties(ciiuCodeOptional.get(), ciiuCodeDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El código CIIU no existe");
        }
        return ciiuCodeDto;
    }

    @Override
    public Page<EconomicActivity> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getActiveCiiuCodes(pagingSort);
    }

    @Override
    public Page<EconomicActivity> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "ciiu_code_id";
        int page = 0;
        int size = 6;
        String id = null;
        String ciiuCode = null;
        String description = null;
        String status = null;

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

        if (customQuery.containsKey("ciiuCode")) {
            ciiuCode = "%" + customQuery.get("ciiuCode") + "%";
        }

        if (customQuery.containsKey("description")) {
            description = "%" + customQuery.get("description") + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("id: " + id);
        log.info("ciiuCode: " + ciiuCode);
        log.info("description: " + description);
        log.info("status: " + status);
        log.info("page: " + page);
        log.info("size: " + size);
        log.info("orders: " + orders);
        log.info("sortBy: " + sortBy);

        Page<EconomicActivity> searchResult = repository.searchCiiuCode(id, ciiuCode, description, status, pagingSort);
        log.info("Resultados de la búsqueda: {}", searchResult.getContent());
        return searchResult;
    }


    @Override
    public List<EconomicActivity> getAllEconomicActivity(Map<String, String> customQuery) {
        Integer offset = 1;
        Integer limit = 10;
        String description = "";
        Long ciiuCode = null;

        if (customQuery.containsKey("ciiuCode")) {
            ciiuCode = Long.parseLong(customQuery.get("ciiuCode"));
        }
        if (customQuery.containsKey("description")) {
            description = customQuery.get("description");
        }
        if (customQuery.containsKey("offset")) {
            offset = Integer.parseInt(customQuery.get("offset"));
        }
        if (customQuery.containsKey("limit")) {
            limit = Integer.parseInt(customQuery.get("limit"));
        }

        offset--;

        Pageable pagingSort = PageRequest.of(offset, limit);

        Page<EconomicActivity> entityPage = repository.findByCiiuCodeAndDescriptionContainingIgnoreCase(ciiuCode, description, pagingSort);

        List<EconomicActivityDto> dtoList = entityPage.getContent().stream()
                .map(entity -> {
                    EconomicActivityDto dto = new EconomicActivityDto();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                }).collect(Collectors.toList());

        ListGenericResponse listGenericResponseOut = new ListGenericResponse();
        listGenericResponseOut.setData(Collections.singletonList(dtoList));
        listGenericResponseOut.setStatusCode(HttpStatus.OK.value());
        listGenericResponseOut.setMessage("success.");
        listGenericResponseOut.setCurrentPage(entityPage.getNumber() + 1);
        listGenericResponseOut.setTotalItems(entityPage.getTotalElements());
        listGenericResponseOut.setTotalPages(entityPage.getTotalPages());

        return (List<EconomicActivity>) listGenericResponseOut;
    }

    @Override
    public List<EconomicActivity> getAllEconomic() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener la sucursal");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar la sucursal", e);
        }
    }


}
