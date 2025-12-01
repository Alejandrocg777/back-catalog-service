package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IdentificationTypeBusiness;
import com.asb.backCompanyService.business.Interfaces.TaxLiabilityBusiness;
import com.asb.backCompanyService.model.IdentificationType;
import com.asb.backCompanyService.model.TaxLiability;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.request.prefix}/${app.request.version}${app.request.mappings}/tax-liability")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class TaxLiabilityController {

    private final TaxLiabilityBusiness taxLiability;

    @GetMapping("/no-page/getAll")
    public ResponseEntity<List<TaxLiability>> getAll() {
        log.info("Iniciando endpoint para obtener todas las ciudades");
        List<TaxLiability> tax = taxLiability.getAll();
        return new ResponseEntity<>(tax, HttpStatus.OK);
    }
}
