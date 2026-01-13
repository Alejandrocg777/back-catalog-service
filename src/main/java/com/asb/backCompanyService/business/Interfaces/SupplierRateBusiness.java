package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.GeneralRatesDTO;
import com.asb.backCompanyService.dto.request.SupplierRateRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.SupplierDtoResponse;
import com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO;
import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.model.SupplierRate;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface SupplierRateBusiness {

    SupplierRate createRate(SupplierRateRequestDTO createDTO);

    Page<SupplierRateResponseDTO> getAll(int page, int size, String orders, String sortBy);

    GenericResponse deleteRate(Long id);

    GenericResponse updateRate(Long id, SupplierRateRequestDTO createDTO);

    Page<SupplierRateResponseDTO> search(Map<String, String> customQuery);

    GenericResponse generateRates( GeneralRatesDTO rate);

}
