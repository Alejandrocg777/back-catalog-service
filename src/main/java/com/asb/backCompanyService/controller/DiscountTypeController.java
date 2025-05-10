package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.DiscountTypeBusiness;
import com.asb.backCompanyService.dto.request.DiscountTypeRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.DiscountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/discoutn-type")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class DiscountTypeController {

    private final DiscountTypeBusiness discountTypeBusiness;

    @PostMapping("/create")
    public ResponseEntity<DiscountTypeRequestDTO> save(@RequestBody DiscountTypeRequestDTO requestDTO) {
        DiscountTypeRequestDTO savedDiscountType = discountTypeBusiness.save(requestDTO);
        return ResponseEntity.ok(savedDiscountType);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody DiscountTypeRequestDTO requestDTO) {
        log.info("Iniciando actualización para Client con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = discountTypeBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        discountTypeBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<DiscountTypeRequestDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        Page<DiscountTypeRequestDTO> clients = discountTypeBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DiscountTypeRequestDTO> get(@PathVariable("id") long id) {
        DiscountTypeRequestDTO requestDTO = discountTypeBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    //@GetMapping("/search")
    //public ResponseEntity<Page<ClientResponseDTO>> search(@RequestParam Map<String, String> customQuery) {
    //    Page<ClientResponseDTO> client = clientBusiness.searchCustom(customQuery);
    //    return ResponseEntity.ok(client);
    //}

    @GetMapping("/no-page/getAllClient")
    public ResponseEntity<List<DiscountType>> getAllNoPage() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<DiscountType> response = discountTypeBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
