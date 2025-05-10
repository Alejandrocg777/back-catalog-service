package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ICurrencyTypeBusiness;
import com.asb.backCompanyService.dto.request.CurrencyTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.CurrencyType;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/currency-type")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class CurrencyTypeController {

    private final ICurrencyTypeBusiness iCurrencyTypeBusiness;

    @PostMapping("/create")
    public ResponseEntity<CurrencyTypeDto> save(@RequestBody CurrencyTypeDto currencyTypeDto) {
        CurrencyTypeDto savedCurrencyType = iCurrencyTypeBusiness.save(currencyTypeDto);
        return ResponseEntity.ok(savedCurrencyType);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CurrencyTypeDto> get(@PathVariable("id") long id) {
        CurrencyTypeDto currencyTypeDto = iCurrencyTypeBusiness.get(id);
        return ResponseEntity.ok(currencyTypeDto);
    }

    @GetMapping
    public ResponseEntity<Page<CurrencyType>> getAll(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(defaultValue = "ASC") String orders,
                                                     @RequestParam(defaultValue = "currency_type_id") String sortBy) {
        Page<CurrencyType> currencyTypes = iCurrencyTypeBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(currencyTypes);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CurrencyType>> search(@RequestParam Map<String, String> customQuery) {
        Page<CurrencyType> currencyTypes = iCurrencyTypeBusiness.searchCurrencyType(customQuery);
        return ResponseEntity.ok(currencyTypes);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long currencyTypeId,
                                                     @RequestBody CurrencyTypeDto currencyTypeDto) {
        log.info("Iniciando actualización para CurrencyType con ID: {} y DTO: {}", currencyTypeId, currencyTypeDto);
        GenericResponse response = iCurrencyTypeBusiness.update(currencyTypeId, currencyTypeDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCurrencyType(@PathVariable Long id) {
        iCurrencyTypeBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllCurrencyTypes")
    public ResponseEntity<List<CurrencyType>> getAllCurrencyTypes() {
        log.info("Starting endpoint to get all currency types");
        List<CurrencyType> currencyTypes = iCurrencyTypeBusiness.getAllCurrencyTypes();
        return new ResponseEntity<>(currencyTypes, HttpStatus.OK);
    }
}
