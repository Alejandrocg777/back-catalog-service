package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ICityBusiness;
import com.asb.backCompanyService.business.Interfaces.IdentificationTypeBusiness;
import com.asb.backCompanyService.model.City;
import com.asb.backCompanyService.model.IdentificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/identification-type")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class IdentificationTypeController {


    private final IdentificationTypeBusiness identificationTypeBusiness;

    @GetMapping("/no-page/getAll")
    public ResponseEntity<List<IdentificationType>> getAll() {
        log.info("Iniciando endpoint para obtener todas las ciudades");
        List<IdentificationType> cities = identificationTypeBusiness.getAll();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
