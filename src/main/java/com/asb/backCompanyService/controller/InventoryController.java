package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IInventoryBusiness;
import com.asb.backCompanyService.dto.request.InventoryDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Inventory;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/inventory")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class  InventoryController {

    private final IInventoryBusiness iInventoryBusiness;

    @PostMapping("/create")
    public ResponseEntity<InventoryDto> save(@RequestBody InventoryDto inventoryDto) {
        InventoryDto savedInventory = iInventoryBusiness.save(inventoryDto);
        return ResponseEntity.ok(savedInventory);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<InventoryDto> get(@PathVariable("id") long id) {
        InventoryDto inventoryDto = iInventoryBusiness.get(id);
        return ResponseEntity.ok(inventoryDto);
    }

    @GetMapping
    public ResponseEntity<Page<Inventory>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size,
                                                  @RequestParam(defaultValue = "ASC") String orders,
                                                  @RequestParam(defaultValue = "id_inventory") String sortBy) {
        Page<Inventory> inventories = iInventoryBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Inventory>> search(@RequestParam Map<String, String> customQuery) {
        Page<Inventory> inventories = iInventoryBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(inventories);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long inventoryId,
                                                     @RequestBody InventoryDto inventoryDto) {
        log.info("Starting update endpoint for inventory with ID: {} and DTO: {}", inventoryId, inventoryDto);
        GenericResponse response = iInventoryBusiness.update(inventoryId, inventoryDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        iInventoryBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllInventory")
    public ResponseEntity<List<Inventory>> getAllInventories() {
        log.info("Starting endpoint to get all inventories");
        List<Inventory> inventories = iInventoryBusiness.getAllInventories();
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }
}
