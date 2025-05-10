package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.CashRegisterRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.CashRegister;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CashRegisterBusiness {

    CashRegisterRequestDTO save(CashRegisterRequestDTO requestDTO);
    GenericResponse update(Long id, CashRegisterRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<CashRegisterRequestDTO>getAll(int page, int size, String orders, String sortBy);
    CashRegisterRequestDTO get(Long id);
    List<CashRegister>  getAllNoPage();

}
