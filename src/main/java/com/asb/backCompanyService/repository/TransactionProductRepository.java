package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.TransactionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionProductRepository extends JpaRepository<TransactionProduct, Long> {

    @Query(value = "SELECT t FROM  TransactionProduct t WHERE t.transactionId = :transactionId ")
    List<TransactionProduct> getQuantityByTransactionId(@Param("transactionId") Long transactionId);
}
