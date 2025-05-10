package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.DiscountRequestDTO;
import com.asb.backCompanyService.dto.responde.DiscountResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Discount;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountBusiness {

    DiscountRequestDTO save(DiscountRequestDTO requestDTO);
    GenericResponse update(Long id, DiscountRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<DiscountResponseDTO> getAll(int page, int size, String orders, String sortBy);
    DiscountRequestDTO get(Long id);
    List<Discount> getAllNoPage();
    //Page<ClientResponseDTO>searchCustom(Map<String , String> customQuery);
}
