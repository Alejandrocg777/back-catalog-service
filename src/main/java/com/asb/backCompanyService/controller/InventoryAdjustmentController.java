package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IInventoryAdjustmentBusiness;
import com.asb.backCompanyService.dto.request.InventoryAdjustmentDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ListGenericResponse;
import com.asb.backCompanyService.model.InventoryAdjustment;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/inventory-adjustment")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class InventoryAdjustmentController {

    private final IInventoryAdjustmentBusiness iInventoryAdjustmentBusiness;

    @PostMapping("/create")
    public ResponseEntity<InventoryAdjustmentDto> save(@RequestBody InventoryAdjustmentDto inventoryAdjustmentDto) {
        InventoryAdjustmentDto savedInventoryAdjustment = iInventoryAdjustmentBusiness.save(inventoryAdjustmentDto);
        return ResponseEntity.ok(savedInventoryAdjustment);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<InventoryAdjustmentDto> get(@PathVariable("id") long id) {
        InventoryAdjustmentDto inventoryAdjustmentDto = iInventoryAdjustmentBusiness.get(id);
        return ResponseEntity.ok(inventoryAdjustmentDto);
    }

    @GetMapping
    public ResponseEntity<Page<InventoryAdjustment>> getAll(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "ASC") String orders,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        Page<InventoryAdjustment> inventoryAdjustments = iInventoryAdjustmentBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(inventoryAdjustments);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<InventoryAdjustment>> search(@RequestParam Map<String, String> customQuery) {
        Page<InventoryAdjustment> inventoryAdjustments = iInventoryAdjustmentBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(inventoryAdjustments);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long inventoryAdjustmentId,
                                                  @RequestBody InventoryAdjustmentDto inventoryAdjustmentDto) {
        log.info("Iniciando actualización para InventoryAdjustment con ID: {} y DTO: {}", inventoryAdjustmentId, inventoryAdjustmentDto);
        GenericResponse response = iInventoryAdjustmentBusiness.update(inventoryAdjustmentId, inventoryAdjustmentDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInventoryAdjustment(@PathVariable Long id) {
        iInventoryAdjustmentBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getInventoryAdjustment")
    public ResponseEntity<ListGenericResponse> getAllInventoryAdjustments(@RequestParam Map<String, String> customQuery) {
        log.info("El endpoint para obtener una lista de ajustes de inventario ha sido iniciado");
        ListGenericResponse response = (ListGenericResponse) iInventoryAdjustmentBusiness.getAllInventoryAdjustments(customQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/no-page/getAllInventoryAdjustments")
    public ResponseEntity<List<InventoryAdjustment>> getAllInventoryAdjustments() {
        log.info("Iniciando endpoint para obtener todos los ajustes de inventario");
        List<InventoryAdjustment> inventoryAdjustments = iInventoryAdjustmentBusiness.getAllInventoryAdjustments();
        return new ResponseEntity<>(inventoryAdjustments, HttpStatus.OK);
    }
}
