package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.SupplierRateBusiness;
import com.asb.backCompanyService.dto.request.GeneralRatesDTO;
import com.asb.backCompanyService.dto.request.SupplierRateRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO;
import com.asb.backCompanyService.model.SupplierRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/supplier-rate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class SupplierRateController {

    private final SupplierRateBusiness supplierRateBusiness;



    @PostMapping("/create")
    public ResponseEntity<SupplierRate> createRate(@RequestBody SupplierRateRequestDTO createDTO) {
        log.info("Iniciando el endpoint para crear proveedor");
        SupplierRate created = supplierRateBusiness.createRate(createDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<SupplierRateResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "ASC") String orders,
                                                                @RequestParam(defaultValue = "id") String sortBy) {
        Page<SupplierRateResponseDTO> response = supplierRateBusiness.getAll(page, size, orders, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable("id") Long id) {
        return new ResponseEntity<>(supplierRateBusiness.deleteRate(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse>updateRate(@PathVariable("id") Long id,
                                                     @RequestBody SupplierRateRequestDTO createDTO){
        return new ResponseEntity<>(supplierRateBusiness.updateRate(id, createDTO), HttpStatus.OK);
    }

    @PostMapping("/general-rates")
    public ResponseEntity<GenericResponse> generateRates(@RequestBody GeneralRatesDTO rate){
        return new ResponseEntity<>(supplierRateBusiness.generateRates(rate), HttpStatus.OK);
    }

}
