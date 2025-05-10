package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.CurrencyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long> {

    @Query(value = "SELECT * FROM currency_type " +
            "WHERE CAST(currency_type_id AS char) LIKE :id " +
            "OR UPPER(description) LIKE UPPER(:description) " +
            "OR UPPER(status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) FROM currency_type " +
                    "WHERE CAST(currency_type_id AS char) LIKE :id " +
                    "OR UPPER(description) LIKE UPPER(:description) " +
                    "OR UPPER(status) LIKE UPPER(:status)",
            nativeQuery = true)
    Page<CurrencyType> searchCurrencyType(String id, String description, String status, Pageable pageable);

    @Query(value = "SELECT * FROM currency_type " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<CurrencyType> getStatus(Pageable pageable);

    boolean existsById(Long id);
}
