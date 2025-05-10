package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.CurrencyTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.CurrencyType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ICurrencyTypeBusiness {
    CurrencyTypeDto save(CurrencyTypeDto currencyTypeDto);

    GenericResponse update(Long id, CurrencyTypeDto currencyTypeDto);

    boolean delete(Long id);

    CurrencyTypeDto get(Long id);

    Page<CurrencyType> getAll(int page, int size, String orders, String sortBy);

    Page<CurrencyType> searchCurrencyType(Map<String, String> customQuery);

    List<CurrencyType> getAllCurrencyTypes();
}
