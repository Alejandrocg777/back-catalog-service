package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.responde.TransactionResponseDTO;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TransactionBusiness {

    Page<TransactionResponseDTO> getTransactions(Integer page,
                                                 Integer size,
                                                  String orders,
                                                 String sortBy);

    Page<TransactionResponseDTO> searchCustom(Map<String, String> customQuery);

}
