package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ClientBusiness;
import com.asb.backCompanyService.dto.request.ClientRequestDTO;
import com.asb.backCompanyService.dto.responde.CityDtoResponse;
import com.asb.backCompanyService.dto.responde.ClientResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Client;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/client")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class ClientController {

    private final ClientBusiness clientBusiness;

    @PostMapping("/create")
    public ResponseEntity<ClientRequestDTO> save(@RequestBody ClientRequestDTO requestDTO) {
        ClientRequestDTO savedClient = clientBusiness.save(requestDTO);
        return ResponseEntity.ok(savedClient);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody ClientRequestDTO requestDTO) {
        log.info("Iniciando actualización para Client con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = clientBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        clientBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        Page<ClientResponseDTO> clients = clientBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ClientRequestDTO> get(@PathVariable("id") long id) {
        ClientRequestDTO requestDTO = clientBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ClientResponseDTO>> search(@RequestParam Map<String, String> customQuery) {
        Page<ClientResponseDTO> client = clientBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/no-page/getAllClient")
    public ResponseEntity<List<ClientResponseDTO>> getAllNoPage(@RequestParam Map<String, String> customQuery) {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<ClientResponseDTO> response = clientBusiness.getAllNoPage(customQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
