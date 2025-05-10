package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.SellerBusiness;
import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Seller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/seller")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j

public class Sellercontroller {

    private final SellerBusiness sellerBusiness;

    @PostMapping("/create")
    public ResponseEntity<SellerRequestDTO> save(@RequestBody SellerRequestDTO requestDTO) {
        SellerRequestDTO savedSeller = sellerBusiness.save(requestDTO);
        return ResponseEntity.ok(savedSeller);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody SellerRequestDTO requestDTO) {
        log.info("Iniciando actualización para City con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = sellerBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        sellerBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<SellerRequestDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size,
                                                                   @RequestParam(defaultValue = "ASC") String orders,
                                                                   @RequestParam(defaultValue = "id") String sortBy) {
        Page<SellerRequestDTO> cities = sellerBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SellerRequestDTO> get(@PathVariable("id") long id) {
        SellerRequestDTO requestDTO = sellerBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    @GetMapping("/no-page/getAllSeller")
    public ResponseEntity<List<Seller>> getAllNopage() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<Seller> response = sellerBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
