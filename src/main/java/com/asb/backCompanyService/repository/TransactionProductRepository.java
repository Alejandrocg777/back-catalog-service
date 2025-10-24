package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.TransactionProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionProductRepository extends JpaRepository<TransactionProduct, Long> {
}
