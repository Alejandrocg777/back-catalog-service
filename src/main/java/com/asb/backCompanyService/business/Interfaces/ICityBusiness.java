package com.asb.backCompanyService.business.Interfaces;


import com.asb.backCompanyService.dto.request.CityDto;
import com.asb.backCompanyService.dto.responde.CityDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.City;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

public interface ICityBusiness {
    CityDto save(CityDto cityDto);


    GenericResponse update(Long id, CityDto cityDto);


    boolean delete(Long id);


    CityDto get(Long id);


    Page<CityDtoResponse> getAll(int page, int size, String orders, String sortBy);

    Page<CityDtoResponse> searchCustom(Map<String, String> customQuery);

    List<City> getAllCity();
}
