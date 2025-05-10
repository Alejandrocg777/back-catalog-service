package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.EconomicActivityDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.EconomicActivity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IEconomicActivityBusiness {
    public EconomicActivityDto save(EconomicActivityDto ciiuCode);

    public GenericResponse update(long id, EconomicActivityDto user);

    boolean delete(Long id);

    EconomicActivityDto get(long id);

    Page<EconomicActivity> getAll(int page, int size, String orders, String sortBy);

    Page<EconomicActivity> searchCustom(Map<String, String> customQuery);

    List<EconomicActivity> getAllEconomicActivity(Map<String, String> customQuery);

    List<EconomicActivity> getAllEconomic();



}
