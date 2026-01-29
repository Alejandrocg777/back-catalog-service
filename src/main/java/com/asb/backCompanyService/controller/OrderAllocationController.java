package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IOrderAllocationBusiness;
import com.asb.backCompanyService.dto.request.OrderAllocationDto;
import com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/order-allocation")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class OrderAllocationController {

    private final IOrderAllocationBusiness iOrderAllocationBusiness;

    @PostMapping("/create")
    public ResponseEntity<OrderAllocationDto> save(@RequestBody OrderAllocationDto orderAllocationDto) {
        log.info("Creando nueva orden de asignación: {}", orderAllocationDto);
        OrderAllocationDto savedOrder = iOrderAllocationBusiness.save(orderAllocationDto);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderAllocationDto> get(@PathVariable("id") Long id) {
        log.info("Obteniendo orden de asignación con ID: {}", id);
        OrderAllocationDto orderAllocationDto = iOrderAllocationBusiness.get(id);
        return ResponseEntity.ok(orderAllocationDto);
    }

    @GetMapping
    public ResponseEntity<Page<OrderAllocationResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ASC") String orders,
            @RequestParam(defaultValue = "id") String sortBy) {
        log.info("Obteniendo todas las órdenes de asignación - Página: {}, Tamaño: {}, Orden: {}, Campo: {}",
                page, size, orders, sortBy);
        Page<OrderAllocationResponseDto> orderAllocations = iOrderAllocationBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(orderAllocations);
    }


    @GetMapping("/get-all-complete")
    public ResponseEntity<Page<OrderAllocationResponseDto>> getAllComplete(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ASC") String orders,
            @RequestParam(defaultValue = "id") String sortBy) {
        log.info("Obteniendo todas las órdenes de asignación - Página: {}, Tamaño: {}, Orden: {}, Campo: {}",
                page, size, orders, sortBy);
        Page<OrderAllocationResponseDto> orderAllocations = iOrderAllocationBusiness.getAllComplete(page, size, orders, sortBy);
        return ResponseEntity.ok(orderAllocations);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<OrderAllocationResponseDto>> search(@RequestParam Map<String, String> customQuery) {
        log.info("Buscando órdenes de asignación con parámetros: {}", customQuery);
        Page<OrderAllocationResponseDto> orderAllocations = iOrderAllocationBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(orderAllocations);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(
            @PathVariable("id") Long id,
            @RequestBody OrderAllocationDto orderAllocationDto) {
        log.info("Actualizando orden de asignación con ID: {} y DTO: {}", id, orderAllocationDto);
        GenericResponse response = iOrderAllocationBusiness.update(id, orderAllocationDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<GenericResponse> updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, String> statusUpdate) {
        log.info("Actualizando estado de orden de asignación ID: {} a estado: {}",
                id, statusUpdate.get("statusOrderAllocation"));

        GenericResponse response = iOrderAllocationBusiness.updateStatus(id, statusUpdate.get("statusOrderAllocation"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-image/{id}")
    public ResponseEntity<GenericResponse> uploadOrderImage(
            @PathVariable("id") Long id,
            @RequestPart("image") MultipartFile image) {

        log.info("📤 Recibiendo imagen para orden ID: {}", id);

        GenericResponse response = iOrderAllocationBusiness.uploadOrderImage(id, image);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-signature/{id}")
    public ResponseEntity<GenericResponse> uploadOrderSignature(
            @PathVariable("id") Long id,
            @RequestPart("signature") MultipartFile signature) {

        log.info(" Recibiendo firma para orden ID: {}", id);

        GenericResponse response = iOrderAllocationBusiness.uploadOrderSignature(id, signature);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderAllocation(@PathVariable Long id) {
        log.info("Eliminando (marcando como inactiva) orden de asignación con ID: {}", id);
        iOrderAllocationBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllOrderAllocations")
    public ResponseEntity<List<OrderAllocationResponseDto>> getAllOrderAllocations() {
        log.info("Obteniendo todas las órdenes de asignación sin paginación");
        List<OrderAllocationResponseDto> orderAllocations = iOrderAllocationBusiness.getAllOrderAllocations();
        return new ResponseEntity<>(orderAllocations, HttpStatus.OK);
    }

    @PostMapping("/getOrderAllocationsByTransporter")
    public ResponseEntity<List<OrderAllocationResponseDto>> getOrderAllocationsByTransporter(
            @RequestBody Map<String, Long> request) {
        log.info("Obteniendo órdenes de asignación para transportador: {}", request.get("transporterId"));

        Long transporterId = request.get("transporterId");
        if (transporterId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<OrderAllocationResponseDto> orderAllocations = iOrderAllocationBusiness.getOrderAllocationsByTransporter(transporterId);

        return new ResponseEntity<>(orderAllocations, HttpStatus.OK);
    }

    @PostMapping("/getOrderComplete")
    public ResponseEntity<List<OrderAllocationResponseDto>> getOrderComplete(
            @RequestBody Map<String, Long> request) {
        log.info("Obteniendo órdenes de completas para transportador: {}", request.get("transporterId"));

        Long transporterId = request.get("transporterId");
        if (transporterId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<OrderAllocationResponseDto> orderAllocations = iOrderAllocationBusiness.getOrderComplete(transporterId);

        return new ResponseEntity<>(orderAllocations, HttpStatus.OK);
    }


}