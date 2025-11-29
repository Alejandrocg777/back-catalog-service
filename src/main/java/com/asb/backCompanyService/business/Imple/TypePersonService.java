package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TypePersonBusiness;
import com.asb.backCompanyService.model.IdentificationType;
import com.asb.backCompanyService.model.TypePerson;
import com.asb.backCompanyService.repository.IdentificationTypeRepository;
import com.asb.backCompanyService.repository.TypePersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j

public class TypePersonService implements TypePersonBusiness {

    private final TypePersonRepository typePersonRepository;

    @Override
    public List<TypePerson> getAll() {
        try {
            return typePersonRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el inventario");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el inventario", e);
        }
    }
}
