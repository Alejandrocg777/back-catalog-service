package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.DiscountBusiness;
import com.asb.backCompanyService.dto.request.DiscountRequestDTO;
import com.asb.backCompanyService.dto.responde.DiscountResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Discount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/discount")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class DiscountController {

    private final DiscountBusiness discountBusiness;

    @PostMapping("/create")
    public ResponseEntity<DiscountRequestDTO> save(@RequestBody DiscountRequestDTO requestDTO) {
        DiscountRequestDTO savedDiscount = discountBusiness.save(requestDTO);
        return ResponseEntity.ok(savedDiscount);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody DiscountRequestDTO requestDTO) {
        log.info("Iniciando actualización para Client con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = discountBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        discountBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<DiscountResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size,
                                                            @RequestParam(defaultValue = "ASC") String orders,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        Page<DiscountResponseDTO> clients = discountBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DiscountRequestDTO> get(@PathVariable("id") long id) {
        DiscountRequestDTO requestDTO = discountBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    //@GetMapping("/search")
    //public ResponseEntity<Page<ClientResponseDTO>> search(@RequestParam Map<String, String> customQuery) {
    //   Page<ClientResponseDTO> client = clientBusiness.searchCustom(customQuery);
    //    return ResponseEntity.ok(client);
    //}

    @GetMapping("/no-page/getAllDiscount")
    public ResponseEntity<List<Discount>> getAllNoPage() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<Discount> response = discountBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
