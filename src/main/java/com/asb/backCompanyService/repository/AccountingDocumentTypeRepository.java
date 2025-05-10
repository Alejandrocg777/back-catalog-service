package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.AccountingDocumentType;
import com.asb.backCompanyService.model.CurrencyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountingDocumentTypeRepository extends JpaRepository<AccountingDocumentType, Long> {

    @Query(value = "SELECT * FROM accounting_document_type " +
            "WHERE CAST(id AS char) LIKE :id " +
            "OR UPPER(description) LIKE UPPER(:description) " +
            "OR UPPER(status) LIKE UPPER(:status) ",
            countQuery = "SELECT COUNT(*) FROM accounting_document_type " +
                    "WHERE CAST(id AS char) LIKE :id " +
                    "OR UPPER(description) LIKE UPPER(:description) " +
                    "OR UPPER(status) LIKE UPPER(:status) ",
            nativeQuery = true)
    Page<AccountingDocumentType> search(String id, String description, String status, Pageable pageable);

    @Query(value = "SELECT * FROM accounting_document_type " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<AccountingDocumentType> getStatus(Pageable pageable);
}
