package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.TaxConfigurationRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.TaxConfiguration;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaxBusiness {

    TaxConfigurationRequestDTO saveTaxConfiguration(TaxConfigurationRequestDTO taxConfiguration);

    GenericResponse update(Long taxId, TaxConfigurationRequestDTO requestDTO);

    Boolean delete(Long id);

    Page<TaxConfiguration> getAll(int page, int size, String orders, String sortBy);

    TaxConfigurationRequestDTO get(Long id);

    List<TaxConfiguration>getAllTax();

}