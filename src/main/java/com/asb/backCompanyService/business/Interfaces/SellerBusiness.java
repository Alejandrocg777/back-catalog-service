package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.CashRegisterRequestDTO;
import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.CashRegister;
import com.asb.backCompanyService.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SellerBusiness {

    SellerRequestDTO save(SellerRequestDTO requestDTO);
    GenericResponse update(Long id, SellerRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<SellerRequestDTO> getAll(int page, int size, String orders, String sortBy);
    SellerRequestDTO get(Long id);
    List<Seller> getAllNoPage();
}
