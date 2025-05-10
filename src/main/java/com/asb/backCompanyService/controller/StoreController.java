package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.StoreBusiness;
import com.asb.backCompanyService.dto.StoreDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Store;
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
@RequestMapping("${app.request.prefix}/${app.request.version}${app.request.mappings}/store")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class StoreController {

    private final StoreBusiness storeBusiness;

    @PostMapping("/create")
    public ResponseEntity<StoreDto> save(@RequestBody StoreDto requestDto) {
        StoreDto action = storeBusiness.save(requestDto);
        return ResponseEntity.ok(action);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<StoreDto> get(@PathVariable("id") long id) {
        StoreDto action = storeBusiness.get(id);
        return ResponseEntity.ok(action);
    }

    @GetMapping
    public ResponseEntity<Page<Store>> getAll(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "5") int size,
                                              @RequestParam(defaultValue = "ASC") String orders,
                                              @RequestParam(defaultValue = "store_id") String sortBy) {
        Page<Store> action = storeBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(action);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Store>> search(@RequestParam Map<String, String> customQuery) {
        Page<Store> action = storeBusiness.search(customQuery);
        return ResponseEntity.ok(action);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long idAction,
                                                  @RequestBody StoreDto action) {
        log.info("Iniciando actualización para City con ID: {} y DTO: {}", idAction, action);
        GenericResponse response = storeBusiness.update(idAction, action);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse> deleteCity(@PathVariable Long id) {
        storeBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllStore")
    public ResponseEntity<List<Store>> getAllCities() {
        log.info("Iniciando endpoint para obtener todo");
        List<Store> actionSteps = storeBusiness.getAllWithoutPagination();
        return new ResponseEntity<>(actionSteps, HttpStatus.OK);
    }
}
