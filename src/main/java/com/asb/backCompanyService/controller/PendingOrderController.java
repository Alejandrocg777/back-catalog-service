package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IPendingOrderBusiness;
import com.asb.backCompanyService.dto.request.PendingOrderRequestDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.PendingOrderProductDtoResponse;
import com.asb.backCompanyService.dto.responde.PendingOrderResponseDto;
import com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse;
import com.asb.backCompanyService.model.PendingOrder;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/pending-order")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class PendingOrderController {

    private final IPendingOrderBusiness pendingOrderBusiness;

    @PostMapping("/create")
    public ResponseEntity<PendingOrderRequestDto> save(@RequestBody PendingOrderRequestDto requestDTO) {
        PendingOrderRequestDto savedPendingOrder = pendingOrderBusiness.save(requestDTO);
        return ResponseEntity.ok(savedPendingOrder);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody PendingOrderRequestDto requestDTO) {
        log.info("Iniciando actualización para PendingOrder con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = pendingOrderBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        pendingOrderBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PendingOrderResponseDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size,
                                                               @RequestParam(defaultValue = "ASC") String orders,
                                                               @RequestParam(defaultValue = "id") String sortBy) {
        Page<PendingOrderResponseDto> PendingOrders = pendingOrderBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(PendingOrders);
    }


    @GetMapping("/getAllProductsByPendingOrder/{pendingOrderId}")
    public ResponseEntity<Page<PendingOrderProductDtoResponse>> getAllProductsByPendingOrder(
            @PathVariable Long pendingOrderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "ASC") String orders,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<PendingOrderProductDtoResponse> products = pendingOrderBusiness.getAllProductsByPendingOrder(pendingOrderId, page, size, orders, sortBy);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<PendingOrderRequestDto> get(@PathVariable("id") long id) {
        PendingOrderRequestDto requestDTO = pendingOrderBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<PendingOrderResponseDto>> search(@RequestParam Map<String, String> customQuery) {
        Page<PendingOrderResponseDto> PendingOrder = pendingOrderBusiness.search(customQuery);
        return ResponseEntity.ok(PendingOrder);
    }

    @GetMapping("/products/search/{pendingOrderId}")
    public ResponseEntity<Page<PendingOrderProductDtoResponse>> searchProductsByPendingOrder(@PathVariable Long pendingOrderId, @RequestParam Map<String, String> customQuery) {
        Page<PendingOrderProductDtoResponse> products = pendingOrderBusiness.searchProductsByPendingOrder(pendingOrderId, customQuery);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/no-page/getAllPendingOrder")
    public ResponseEntity<List<PendingOrderRequestDto>> getAllNoPage() {
        log.info("Iniciando endpoint para obtener todas las empleados");
        List<PendingOrderRequestDto> response = pendingOrderBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
