package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.CashRegisterBusiness;
import com.asb.backCompanyService.dto.request.CashRegisterRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.CashRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/cash-register")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class CashRegisterController {


    private final CashRegisterBusiness cashRegisterBusiness;

    @PostMapping("/create")
    public ResponseEntity<CashRegisterRequestDTO> save(@RequestBody CashRegisterRequestDTO requestDTO) {
        CashRegisterRequestDTO savedCashRegister = cashRegisterBusiness.save(requestDTO);
        return ResponseEntity.ok(savedCashRegister);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody CashRegisterRequestDTO requestDTO) {
        log.info("Iniciando actualización para la caja con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = cashRegisterBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cashRegisterBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CashRegisterRequestDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size,
                                                                   @RequestParam(defaultValue = "ASC") String orders,
                                                                   @RequestParam(defaultValue = "id") String sortBy) {
        Page<CashRegisterRequestDTO> response = cashRegisterBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CashRegisterRequestDTO> get(@PathVariable("id") long id) {
        CashRegisterRequestDTO response = cashRegisterBusiness.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/no-page/getAllCash-register")
    public ResponseEntity<List<CashRegister>> getAllNopage() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<CashRegister> response = cashRegisterBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
