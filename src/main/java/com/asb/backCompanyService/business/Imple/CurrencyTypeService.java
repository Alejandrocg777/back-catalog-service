package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ICurrencyTypeBusiness;
import com.asb.backCompanyService.dto.request.CurrencyTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.CurrencyType;
import com.asb.backCompanyService.repository.CurrencyTypeRepository;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CurrencyTypeService implements ICurrencyTypeBusiness {

    private final CurrencyTypeRepository repository;

    @Override
    @Transactional
    public CurrencyTypeDto save(CurrencyTypeDto currencyTypeDto) {
        try {
            log.info("Guardando CurrencyType: {}", currencyTypeDto);
            boolean objectExists = false;
            if (currencyTypeDto.getId() != null) {
                objectExists = repository.existsById(currencyTypeDto.getId());
            }

            if (!objectExists) {
                CurrencyType currencyTypeRepo = new CurrencyType();
                currencyTypeRepo.setDescription(currencyTypeDto.getDescription());
                currencyTypeRepo.setStatus("ACTIVE");

                CurrencyType newObject = repository.save(currencyTypeRepo);

                CurrencyTypeDto objectDtoVo = new CurrencyTypeDto();
                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de moneda ya existe");
            }
        } catch (Exception e) {
            log.error("Error al guardar CurrencyType", e);
            throw new RuntimeException("Error al guardar el tipo de moneda", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, CurrencyTypeDto currencyTypeDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando método de actualización para CurrencyType con ID: {} y CurrencyTypeDto: {}", id, currencyTypeDto);

            Optional<CurrencyType> optionalCurrencyType = repository.findById(id);
            if (!optionalCurrencyType.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de moneda no existe");
            }

            CurrencyType currencyType = optionalCurrencyType.get();
            BeanUtils.copyProperties(currencyTypeDto, currencyType);
            currencyType.setDescription(currencyTypeDto.getDescription());
            repository.save(currencyType);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Tipo de moneda actualizado");
        } catch (Exception e) {
            log.error("Error al actualizar el tipo de moneda: {}", e.getMessage());
            throw new RuntimeException("Error", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            CurrencyType currencyType = repository.findById(id).get();
            currencyType.setStatus("INACTIVE");
            repository.save(currencyType);
            return true;
        } else {
            throw new RuntimeException("El tipo de moneda no fue encontrado por el id " + id);
        }
    }

    @Override
    public CurrencyTypeDto get(Long id) {
        Optional<CurrencyType> currencyTypeOptional = repository.findById(id);
        CurrencyTypeDto currencyTypeDto = null;
        if (currencyTypeOptional.isPresent()) {
            currencyTypeDto = new CurrencyTypeDto();
            BeanUtils.copyProperties(currencyTypeOptional.get(), currencyTypeDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de moneda no existe");
        }
        return currencyTypeDto;
    }

    @Override
    public Page<CurrencyType> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getStatus(pagingSort);
    }

    @Override
    public Page<CurrencyType> searchCurrencyType(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "currency_type_id";
        int page = 0;
        int size = 6;
        String id = null;
        String description = null;
        String status = null;

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }
        if (customQuery.containsKey("description")) {
            description = "%" + customQuery.get("description") + "%";
        }
        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("ID: " + id);
        log.info("Description: " + description);
        log.info("Status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<CurrencyType> searchResult = repository.searchCurrencyType(id, description, status, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<CurrencyType> getAllCurrencyTypes() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener los tipos de moneda");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se pueden recuperar los tipos de moneda", e);
        }
    }
}
