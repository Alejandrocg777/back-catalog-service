package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.DiscountTypeRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Client;
import com.asb.backCompanyService.model.DiscountType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountTypeBusiness {

    DiscountTypeRequestDTO save(DiscountTypeRequestDTO requestDTO);
    GenericResponse update(Long id, DiscountTypeRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<DiscountTypeRequestDTO> getAll(int page, int size, String orders, String sortBy);
    DiscountTypeRequestDTO get(Long id);
    List<DiscountType> getAllNoPage();
    //Page<ClientResponseDTO>searchCustom(Map<String , String> customQuery);
}
