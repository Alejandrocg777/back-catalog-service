package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ICityBusiness;
import com.asb.backCompanyService.dto.request.CityDto;
import com.asb.backCompanyService.dto.responde.CityDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.City;
import jakarta.annotation.PostConstruct;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/city")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class CityController {

    private final ICityBusiness iCityBusiness;

    @PostMapping("/create")
    public ResponseEntity<CityDto> save(@RequestBody CityDto cityDto) {
        CityDto savedCity = iCityBusiness.save(cityDto);
        return ResponseEntity.ok(savedCity);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CityDto> get(@PathVariable("id") long id) {
        CityDto cityDto = iCityBusiness.get(id);
        return ResponseEntity.ok(cityDto);
    }

    @GetMapping
    public ResponseEntity<Page<CityDtoResponse>> getAll(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        Page<CityDtoResponse> cities = iCityBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CityDtoResponse>> search(@RequestParam Map<String, String> customQuery) {
        Page<CityDtoResponse> city = iCityBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(city);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long cityId,
                                                     @RequestBody CityDto cityDto) {
        log.info("Iniciando actualización para City con ID: {} y DTO: {}", cityId, cityDto);
        GenericResponse response = iCityBusiness.update(cityId, cityDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        iCityBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllCities")
    public ResponseEntity<List<City>> getAllCities() {
        log.info("Iniciando endpoint para obtener todas las ciudades");
        List<City> cities = iCityBusiness.getAllCity();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
