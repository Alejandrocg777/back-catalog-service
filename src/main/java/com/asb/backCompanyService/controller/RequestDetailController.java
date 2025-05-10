package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IRequestDetailBusiness;
import com.asb.backCompanyService.dto.request.RequestDetailDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.RequestDetails;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/request-details")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class RequestDetailController {

    private final IRequestDetailBusiness iRequestDetailBusiness;

    @PostMapping("/create")
    public ResponseEntity<String> save(@RequestBody RequestDetailDto requestDetailDto) {
        RequestDetailDto savedRequestDetail = iRequestDetailBusiness.save(requestDetailDto);
        return ResponseEntity.ok("Detalle de solicitud creado con éxito: " + savedRequestDetail);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> get(@PathVariable("id") long id) {
        RequestDetailDto requestDetailDto = iRequestDetailBusiness.get(id);
        return ResponseEntity.ok("Detalle de solicitud recuperado: " + requestDetailDto);
    }

    @GetMapping
    public ResponseEntity<Page<RequestDetails>> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size,
                                                       @RequestParam(defaultValue = "ASC") String orders,
                                                       @RequestParam(defaultValue = "id") String sortBy) {
        Page<RequestDetails> requestDetails = iRequestDetailBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(requestDetails);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RequestDetails>> search(@RequestParam Map<String, String> customQuery) {
        Page<RequestDetails> requestDetails = iRequestDetailBusiness.searchRequestDetails(customQuery);
        return ResponseEntity.ok(requestDetails);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") long requestDetailId,
                                         @RequestBody RequestDetailDto requestDetailDto) {
        log.info("Iniciando actualización para RequestDetail con ID: {} y DTO: {}", requestDetailId, requestDetailDto);
        GenericResponse response = iRequestDetailBusiness.update(requestDetailId, requestDetailDto);
        return ResponseEntity.ok("Detalle de solicitud actualizado con éxito: " + response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRequestDetail(@PathVariable Long id) {
        iRequestDetailBusiness.delete(id);
        return ResponseEntity.ok("Detalle de solicitud eliminado con éxito");
    }

    @GetMapping("/no-page/getAllRequestDetails")
    public ResponseEntity<List<RequestDetails>> getAllRequestDetails() {
        log.info("Iniciando el endpoint para obtener todos los detalles de las solicitudes");
        List<RequestDetails> requestDetails = iRequestDetailBusiness.getAllRequestDetails();
        return new ResponseEntity<>(requestDetails, HttpStatus.OK);
    }
}
