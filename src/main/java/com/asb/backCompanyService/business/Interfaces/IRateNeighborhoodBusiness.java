package com.asb.backCompanyService.business.Interfaces;


import com.asb.backCompanyService.dto.request.GeneralRateNeighborhoodDto;
import com.asb.backCompanyService.dto.request.GeneralRatesDTO;
import com.asb.backCompanyService.dto.request.RateNeighborhoodDto;
import com.asb.backCompanyService.dto.responde.RateNeighborhoodDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.RateNeighborhood;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IRateNeighborhoodBusiness {
    RateNeighborhoodDto save(RateNeighborhoodDto RateNeighborhoodDto);


    GenericResponse update(Long id, RateNeighborhoodDto RateNeighborhoodDto);


    GeneralRateNeighborhoodDto generateRates( GeneralRateNeighborhoodDto rate);

    boolean delete(Long id);


    RateNeighborhoodDto get(Long id);


    Page<RateNeighborhoodDtoResponse> getAll(int page, int size, String orders, String sortBy);

    Page<RateNeighborhoodDtoResponse> searchCustom(Map<String, String> customQuery);

    List<RateNeighborhood> getAllRateNeighborhood();
}
