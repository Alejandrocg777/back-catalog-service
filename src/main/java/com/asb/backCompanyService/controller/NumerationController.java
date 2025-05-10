package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.INumerationBusiness;
import com.asb.backCompanyService.dto.request.NumerationDto;
import com.asb.backCompanyService.dto.responde.NumerationResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Numeration;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/numeration")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class NumerationController {

    private final INumerationBusiness iNumerationBusiness;

    @PostMapping("/create")
    public ResponseEntity<NumerationDto> save(@RequestBody NumerationDto numerationDto) {
        NumerationDto savedNumeration = iNumerationBusiness.save(numerationDto);
        return ResponseEntity.ok(savedNumeration);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<NumerationDto> get(@PathVariable("id") long id) {
        NumerationDto numerationDto = iNumerationBusiness.get(id);
        return ResponseEntity.ok(numerationDto);
    }

    @GetMapping
    public ResponseEntity<Page<NumerationResponseDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size,
                                                              @RequestParam(defaultValue = "ASC") String orders,
                                                              @RequestParam(defaultValue = "id") String sortBy) {
        Page<NumerationResponseDto> numerations = iNumerationBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(numerations);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NumerationResponseDto>> search(@RequestParam Map<String, String> customQuery) {
        Page<NumerationResponseDto> numerations = iNumerationBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(numerations);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long numerationId,
                                                  @RequestBody NumerationDto numerationDto) {
        log.info("Iniciando actualización para Numeration con ID: {} y DTO: {}", numerationId, numerationDto);
        GenericResponse response = iNumerationBusiness.update(numerationId, numerationDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNumeration(@PathVariable Long id) {
        iNumerationBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllNumerations")
    public ResponseEntity<List<NumerationResponseDto>> getAllNumerations() {
        log.info("Iniciando endpoint para obtener todas las numeraciones");
        List<NumerationResponseDto> numerations = iNumerationBusiness.getAllNumeration();
        return new ResponseEntity<>(numerations, HttpStatus.OK);
    }
}
