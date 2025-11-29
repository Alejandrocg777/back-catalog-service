package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IdentificationTypeBusiness;
import com.asb.backCompanyService.model.City;
import com.asb.backCompanyService.model.IdentificationType;
import com.asb.backCompanyService.repository.IdentificationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j

public class IdentificationTypeService implements IdentificationTypeBusiness {


    private final IdentificationTypeRepository identificationTypeRepository;

    @Override
    public List<IdentificationType> getAll() {
        try {
            return identificationTypeRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el inventario");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el inventario", e);
        }
    }
}
