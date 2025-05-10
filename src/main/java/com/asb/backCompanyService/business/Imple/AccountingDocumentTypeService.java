package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IAccountingDocumentTypeBusiness;
import com.asb.backCompanyService.dto.request.AccountingDocumentTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.AccountingDocumentType;
import com.asb.backCompanyService.model.CurrencyType;
import com.asb.backCompanyService.repository.AccountingDocumentTypeRepository;
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
public class AccountingDocumentTypeService implements IAccountingDocumentTypeBusiness {

    private final AccountingDocumentTypeRepository repository;

    @Override
    @Transactional
    public AccountingDocumentTypeDto save(AccountingDocumentTypeDto accountingDocumentTypeDto) {
        try {
            log.info("Guardando AccountingDocumentType: {}", accountingDocumentTypeDto);
            boolean objectExists = false;
            if (accountingDocumentTypeDto.getId() != null) {
                objectExists = repository.existsById(accountingDocumentTypeDto.getId());
            }

            if (!objectExists) {
                AccountingDocumentType accountingDocumentTypeRepo = new AccountingDocumentType();
                accountingDocumentTypeRepo.setDescription(accountingDocumentTypeDto.getDescription());
                accountingDocumentTypeRepo.setStatus("ACTIVE");

                AccountingDocumentType newObject = repository.save(accountingDocumentTypeRepo);

                AccountingDocumentTypeDto objectDtoVo = new AccountingDocumentTypeDto();
                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de documento contable ya existe");
            }
        } catch (Exception e) {
            log.error("Error al guardar AccountingDocumentType", e);
            throw new RuntimeException("Error al guardar el tipo de documento contable", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, AccountingDocumentTypeDto accountingDocumentTypeDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando método de actualización para AccountingDocumentType con ID: {} y AccountingDocumentTypeDto: {}", id, accountingDocumentTypeDto);

            Optional<AccountingDocumentType> optionalAccountingDocumentType = repository.findById(id);
            if (!optionalAccountingDocumentType.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de documento contable no existe");
            }

            AccountingDocumentType accountingDocumentType = optionalAccountingDocumentType.get();
            BeanUtils.copyProperties(accountingDocumentTypeDto, accountingDocumentType);
            accountingDocumentType.setDescription(accountingDocumentTypeDto.getDescription());
            repository.save(accountingDocumentType);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Tipo de documento contable actualizado");
        } catch (Exception e) {
            log.error("Error al actualizar el tipo de documento contable: {}", e.getMessage());
            throw new RuntimeException("Error", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            AccountingDocumentType accountingDocumentType = repository.findById(id).get();
            accountingDocumentType.setStatus("INACTIVE");
            repository.save(accountingDocumentType);
            return true;
        } else {
            throw new RuntimeException("El tipo de documento contable no fue encontrado por el id " + id);
        }
    }

    @Override
    public AccountingDocumentTypeDto get(Long id) {
        Optional<AccountingDocumentType> accountingDocumentTypeOptional = repository.findById(id);
        AccountingDocumentTypeDto accountingDocumentTypeDto = null;
        if (accountingDocumentTypeOptional.isPresent()) {
            accountingDocumentTypeDto = new AccountingDocumentTypeDto();
            BeanUtils.copyProperties(accountingDocumentTypeOptional.get(), accountingDocumentTypeDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de documento contable no existe");
        }
        return accountingDocumentTypeDto;
    }

    @Override
    public Page<AccountingDocumentType> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getStatus(pagingSort);
    }



    @Override
    public Page<AccountingDocumentType> searchAccountingDocumentTypes(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "accounting_document_type_id";
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

        Page<AccountingDocumentType> searchResult = repository.search(id, description, status, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<AccountingDocumentType> getAllAccountingDocumentTypes() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener los tipos de documentos contables");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se pueden recuperar los tipos de documentos contables", e);
        }
    }
}
