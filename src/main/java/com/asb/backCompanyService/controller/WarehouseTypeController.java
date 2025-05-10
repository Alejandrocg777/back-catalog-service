package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IWarehouseTypeBusiness;
import com.asb.backCompanyService.dto.request.WarehouseTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.WarehouseType;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/warehouse-type")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class WarehouseTypeController {

    private final IWarehouseTypeBusiness iWarehouseTypeBusiness;

    @PostMapping("/create")
    public ResponseEntity<WarehouseTypeDto> save(@RequestBody WarehouseTypeDto warehouseTypeDto) {
        WarehouseTypeDto savedWarehouseType = iWarehouseTypeBusiness.save(warehouseTypeDto);
        return ResponseEntity.ok(savedWarehouseType);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<WarehouseTypeDto> get(@PathVariable("id") long id) {
        WarehouseTypeDto warehouseTypeDto = iWarehouseTypeBusiness.get(id);
        return ResponseEntity.ok(warehouseTypeDto);
    }

    @GetMapping
    public ResponseEntity<Page<WarehouseType>> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size,
                                                      @RequestParam(defaultValue = "ASC") String orders,
                                                      @RequestParam(defaultValue = "id") String sortBy) {
        Page<WarehouseType> warehouseTypes = iWarehouseTypeBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(warehouseTypes);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<WarehouseType>> search(@RequestParam Map<String, String> customQuery) {
        Page<WarehouseType> warehouseTypes = iWarehouseTypeBusiness.searchWarehouseTypes(customQuery);
        return ResponseEntity.ok(warehouseTypes);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long warehouseTypeId,
                                                  @RequestBody WarehouseTypeDto warehouseTypeDto) {
        log.info("Iniciando actualización para WarehouseType con ID: {} y DTO: {}", warehouseTypeId, warehouseTypeDto);
        GenericResponse response = iWarehouseTypeBusiness.update(warehouseTypeId, warehouseTypeDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWarehouseType(@PathVariable Long id) {
        iWarehouseTypeBusiness.delete(id);
        return ResponseEntity.ok("El tipo de almacén ha sido marcado como INACTIVO.");
    }

    @GetMapping("/no-page/getAllWarehouseTypes")
    public ResponseEntity<List<WarehouseType>> getAllWarehouseTypes() {
        log.info("Iniciando el endpoint para obtener todos los tipos de almacén sin paginación");
        List<WarehouseType> warehouseTypes = iWarehouseTypeBusiness.getAllWarehouseTypes();
        return new ResponseEntity<>(warehouseTypes, HttpStatus.OK);
    }
}
