package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IRequestBusiness;
import com.asb.backCompanyService.dto.request.RequestDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Requests;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class RequestController {

    private final IRequestBusiness iRequestBusiness;

    @PostMapping("/create")
    public ResponseEntity<RequestDto> save(@RequestBody RequestDto requestDto) {
        RequestDto savedRequest = iRequestBusiness.save(requestDto);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<RequestDto> get(@PathVariable("id") long id) {
        RequestDto requestDto = iRequestBusiness.get(id);
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping
    public ResponseEntity<Page<Requests>> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size,
                                                 @RequestParam(defaultValue = "ASC") String orders,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        Page<Requests> requests = iRequestBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Requests>> search(@RequestParam Map<String, String> customQuery) {
        Page<Requests> requests = iRequestBusiness.searchRequests(customQuery);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long requestId,
                                                  @RequestBody RequestDto requestDto) {
        log.info("Iniciando actualización para Request con ID: {} y DTO: {}", requestId, requestDto);
        GenericResponse response = iRequestBusiness.update(requestId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
        iRequestBusiness.delete(id);
        return ResponseEntity.ok("Solicitud eliminada exitosamente.");
    }
    @GetMapping("/no-page/getAllRequests")
    public ResponseEntity<List<Requests>> getAllRequests() {
        log.info("Iniciando el endpoint para obtener todas las solicitudes");
        List<Requests> requests = iRequestBusiness.getAllRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
}
