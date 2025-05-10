package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IWarehouseBusiness;
import com.asb.backCompanyService.dto.request.WarehouseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Warehouse;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/warehouse")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

@Slf4j
public class WarehouseController {

    private final IWarehouseBusiness iWarehouseBusiness;

    @PostMapping("/create")
    public ResponseEntity<WarehouseDto> save(@RequestBody WarehouseDto warehouseDto) {
        WarehouseDto savedWarehouse = iWarehouseBusiness.save(warehouseDto);
        return ResponseEntity.ok(savedWarehouse);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<WarehouseDto> get(@PathVariable("id") long id) {
        WarehouseDto warehouseDto = iWarehouseBusiness.get(id);
        return ResponseEntity.ok(warehouseDto);
    }

    @GetMapping
    public ResponseEntity<Page<Warehouse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size,
                                                  @RequestParam(defaultValue = "ASC") String orders,
                                                  @RequestParam(defaultValue = "id") String sortBy) {
        Page<Warehouse> warehouses = iWarehouseBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Warehouse>> search(@RequestParam Map<String, String> customQuery) {
        Page<Warehouse> warehouses = iWarehouseBusiness.searchWarehouses(customQuery);
        return ResponseEntity.ok(warehouses);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long warehouseId,
                                                  @RequestBody WarehouseDto warehouseDto) {
        log.info("Iniciando actualización para Warehouse con ID: {} y DTO: {}", warehouseId, warehouseDto);
        GenericResponse response = iWarehouseBusiness.update(warehouseId, warehouseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable Long id) {
        iWarehouseBusiness.delete(id);
        return ResponseEntity.ok("Bodega eliminada exitosamente.");
    }

    @GetMapping("/no-page/getAllWarehouses")
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        log.info("Iniciando el endpoint para obtener todas las bodegas");
        List<Warehouse> warehouses = iWarehouseBusiness.getAllWarehouses();
        return new ResponseEntity<>(warehouses, HttpStatus.OK);
    }
}
