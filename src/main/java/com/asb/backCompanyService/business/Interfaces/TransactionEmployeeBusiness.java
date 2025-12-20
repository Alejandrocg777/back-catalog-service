package com.asb.backCompanyService.business.Interfaces;


import com.asb.backCompanyService.dto.request.TransactionEmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.TransactionEmployeeResponseDTO;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TransactionEmployeeBusiness {

    TransactionEmployeeResponseDTO createTransaction(TransactionEmployeeRequestDTO requestDTO);

    Page<TransactionEmployeeResponseDTO> getTransactions(Integer page, Integer size, String orders, String sortBy);

    Page<TransactionEmployeeResponseDTO> search(Map<String, String> customQuery);

}
