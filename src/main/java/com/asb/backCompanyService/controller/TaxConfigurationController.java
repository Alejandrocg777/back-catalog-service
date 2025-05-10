package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.TaxBusiness;
import com.asb.backCompanyService.dto.request.TaxConfigurationRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.TaxConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/tax")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class TaxConfigurationController {

    private final TaxBusiness taxBusiness;

    @PostMapping("/create")
    public ResponseEntity<TaxConfigurationRequestDTO> save(@RequestBody TaxConfigurationRequestDTO taxConfiguration) {
        TaxConfigurationRequestDTO savedTaxConfiguration = taxBusiness.saveTaxConfiguration(taxConfiguration);
        return ResponseEntity.ok(savedTaxConfiguration);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long taxId,
                                                  @RequestBody TaxConfigurationRequestDTO requestDTO) {
        log.info("Iniciando actualización para City con ID: {} y DTO: {}", taxId, requestDTO);
        GenericResponse response = taxBusiness.update(taxId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        taxBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<TaxConfiguration>> getAll(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         @RequestParam(defaultValue = "ASC") String orders,
                                                         @RequestParam(defaultValue = "tax_configuration_id") String sortBy) {
        Page<TaxConfiguration> taxConfigurations = taxBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(taxConfigurations);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaxConfigurationRequestDTO> get(@PathVariable("id") long id) {
        TaxConfigurationRequestDTO requestDTO = taxBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    @GetMapping("/no-page/getAllTax")
    public ResponseEntity<List<TaxConfiguration>> getAllCities() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<TaxConfiguration> response = taxBusiness.getAllTax();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
