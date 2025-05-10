package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.MethodPaymentRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.MethodPayment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MethodPaymentBusiness {

    MethodPaymentRequestDTO save(MethodPaymentRequestDTO requestDTO);
    GenericResponse update(Long id, MethodPaymentRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<MethodPaymentRequestDTO> getAll(int page, int size, String orders, String sortBy);
    MethodPaymentRequestDTO get(Long id);
    List<MethodPayment> getAllNoPage();
}
