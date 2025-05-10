package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.NumerationDto;
import com.asb.backCompanyService.dto.responde.NumerationResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface INumerationBusiness {

    NumerationDto save(NumerationDto numerationDto);

    GenericResponse update(Long id, NumerationDto numerationDto);

    boolean delete(Long id);

    NumerationDto get(Long id);

    Page<NumerationResponseDto> getAll(int page, int size, String orders, String sortBy);

    Page<NumerationResponseDto> searchCustom(Map<String, String> customQuery);

    List<NumerationResponseDto> getAllNumeration();
}
