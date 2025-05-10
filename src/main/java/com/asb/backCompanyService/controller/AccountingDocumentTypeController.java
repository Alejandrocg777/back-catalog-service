package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IAccountingDocumentTypeBusiness;
import com.asb.backCompanyService.dto.request.AccountingDocumentTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.AccountingDocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/accounting-document-type")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class AccountingDocumentTypeController {

    private final IAccountingDocumentTypeBusiness iAccountingDocumentTypeBusiness;

    @PostMapping("/create")
    public ResponseEntity<AccountingDocumentTypeDto> save(@RequestBody AccountingDocumentTypeDto dto) {
        AccountingDocumentTypeDto savedDto = iAccountingDocumentTypeBusiness.save(dto);
        return ResponseEntity.ok(savedDto);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AccountingDocumentTypeDto> get(@PathVariable("id") long id) {
        AccountingDocumentTypeDto dto = iAccountingDocumentTypeBusiness.get(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<AccountingDocumentType>> getAll(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size,
                                                               @RequestParam(defaultValue = "ASC") String orders,
                                                               @RequestParam(defaultValue = "accounting_document_type_id") String sortBy) {
        Page<AccountingDocumentType> entities = iAccountingDocumentTypeBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AccountingDocumentType>> search(@RequestParam Map<String, String> customQuery) {
        Page<AccountingDocumentType> entities = iAccountingDocumentTypeBusiness.searchAccountingDocumentTypes(customQuery);
        return ResponseEntity.ok(entities);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long id, @RequestBody AccountingDocumentTypeDto dto) {
        log.info("Iniciando actualización para AccountingDocumentType con ID: {} y DTO: {}", id, dto);
        GenericResponse response = iAccountingDocumentTypeBusiness.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        iAccountingDocumentTypeBusiness.delete(id);
        return ResponseEntity.ok("El tipo de documento contable ha sido marcado como INACTIVO.");
    }

    @GetMapping("/no-page/getAllAccountingDocumentTypes")
    public ResponseEntity<List<AccountingDocumentType>> getAllAccountingDocumentTypes() {
        log.info("Iniciando el endpoint para obtener todos los tipos de documentos contables sin paginación");
        List<AccountingDocumentType> entities = iAccountingDocumentTypeBusiness.getAllAccountingDocumentTypes();
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }
}
