package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO;
import com.asb.backCompanyService.dto.responde.TransactionResponseDTO;
import com.asb.backCompanyService.dto.responde.TransactionResponseNewDTO;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TransactionBusiness {

    Page<TransactionResponseNewDTO> getTransactions(Integer page,
                                                    Integer size,
                                                    String orders,
                                                    String sortBy);

    Page<ProductOfTransactionDTO> getProductsOfTransaction(Long transactionId,
                                                           Integer page,
                                                           Integer size,
                                                           String orders,
                                                           String sortBy);

    Page<TransactionResponseNewDTO> searchCustom(Map<String, String> customQuery);


    Page<ProductOfTransactionDTO> searchProducts(Long transactionId, Map<String, String> customQuery);

}
