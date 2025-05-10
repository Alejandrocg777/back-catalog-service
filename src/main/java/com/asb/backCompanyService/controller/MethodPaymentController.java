package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.MethodPaymentBusiness;
import com.asb.backCompanyService.dto.request.MethodPaymentRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.MethodPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/method-payment")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class MethodPaymentController {

    private final MethodPaymentBusiness methodPaymentBusiness;

    @PostMapping("/create")
    public ResponseEntity<MethodPaymentRequestDTO> save(@RequestBody MethodPaymentRequestDTO requestDTO) {
        MethodPaymentRequestDTO savedMethodPayment = methodPaymentBusiness.save(requestDTO);
        return ResponseEntity.ok(savedMethodPayment);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody MethodPaymentRequestDTO requestDTO) {
        log.info("Iniciando actualización para City con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = methodPaymentBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        methodPaymentBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<MethodPaymentRequestDTO>> getAll(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         @RequestParam(defaultValue = "ASC") String orders,
                                                         @RequestParam(defaultValue = "id") String sortBy) {
        Page<MethodPaymentRequestDTO> cities = methodPaymentBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MethodPaymentRequestDTO> get(@PathVariable("id") long id) {
        MethodPaymentRequestDTO requestDTO = methodPaymentBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    @GetMapping("/no-page/getAllTax")
    public ResponseEntity<List<MethodPayment>> getAllNoPage() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<MethodPayment> response = methodPaymentBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
