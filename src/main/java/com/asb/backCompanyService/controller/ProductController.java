package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ProductBusiness;
import com.asb.backCompanyService.dto.request.ProductRequestDTO;
import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.model.Product;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/product")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class ProductController {

    private final ProductBusiness productBusiness;

    @PostMapping("/create")
    public ResponseEntity<ProductRequestDTO> save(@RequestBody ProductRequestDTO requestDTO) {
        ProductRequestDTO savedSeller = productBusiness.save(requestDTO);
        return ResponseEntity.ok(savedSeller);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody ProductRequestDTO requestDTO) {
        log.info("Iniciando actualización para City con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = productBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        productBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         @RequestParam(defaultValue = "ASC") String orders,
                                                         @RequestParam(defaultValue = "id") String sortBy) {
        Page<ProductResponseDTO> products = productBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponseDTO> get(@PathVariable("id") long id) {
        ProductResponseDTO requestDTO = productBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    @GetMapping("/no-page/getAllProduct")
    public ResponseEntity<List<Product>> getAllNopage() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<Product> response = productBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
