package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IRateNeighborhoodBusiness;
import com.asb.backCompanyService.dto.request.GeneralRateNeighborhoodDto;
import com.asb.backCompanyService.dto.request.RateNeighborhoodDto;
import com.asb.backCompanyService.dto.responde.RateNeighborhoodDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.RateNeighborhood;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/rate-neighborhood")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class RateNeighborhoodController {

    private final IRateNeighborhoodBusiness iRateNeighborhoodBusiness;

    @PostMapping("/create")
    public ResponseEntity<RateNeighborhoodDto> save(@RequestBody RateNeighborhoodDto RateNeighborhoodDto) {
        RateNeighborhoodDto savedRateNeighborhood = iRateNeighborhoodBusiness.save(RateNeighborhoodDto);
        return ResponseEntity.ok(savedRateNeighborhood);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<RateNeighborhoodDto> get(@PathVariable("id") long id) {
        RateNeighborhoodDto RateNeighborhoodDto = iRateNeighborhoodBusiness.get(id);
        return ResponseEntity.ok(RateNeighborhoodDto);
    }

    @GetMapping
    public ResponseEntity<Page<RateNeighborhoodDtoResponse>> getAll(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        Page<RateNeighborhoodDtoResponse> cities = iRateNeighborhoodBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RateNeighborhoodDtoResponse>> search(@RequestParam Map<String, String> customQuery) {
        Page<RateNeighborhoodDtoResponse> RateNeighborhood = iRateNeighborhoodBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(RateNeighborhood);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long RateNeighborhoodId,
                                                     @RequestBody RateNeighborhoodDto RateNeighborhoodDto) {
        log.info("Iniciando actualización para RateNeighborhood con ID: {} y DTO: {}", RateNeighborhoodId, RateNeighborhoodDto);
        GenericResponse response = iRateNeighborhoodBusiness.update(RateNeighborhoodId, RateNeighborhoodDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/general-rates")
    public ResponseEntity<GeneralRateNeighborhoodDto> generateRates(@RequestBody GeneralRateNeighborhoodDto rate){
        return new ResponseEntity<>(iRateNeighborhoodBusiness.generateRates(rate), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRateNeighborhood(@PathVariable Long id) {
        iRateNeighborhoodBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAll")
    public ResponseEntity<List<RateNeighborhood>> getAllNoPage() {
        log.info("Iniciando endpoint para obtener todas las ciudades");
        List<RateNeighborhood> cities = iRateNeighborhoodBusiness.getAllRateNeighborhood();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
