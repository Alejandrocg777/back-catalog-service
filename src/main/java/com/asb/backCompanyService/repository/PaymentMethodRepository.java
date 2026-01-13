package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.CurrencyType;
import com.asb.backCompanyService.model.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query(value = "SELECT * FROM payment_method " +
            "WHERE status = 'ACTIVE' " +
            "AND (CAST(payment_method_id AS char) LIKE :id " +
            "OR UPPER(description) LIKE UPPER(:description))",
            countQuery = "SELECT COUNT(*) FROM payment_method " +
                    "WHERE status = 'ACTIVE' " +
                    "AND (CAST(payment_method_id AS char) LIKE :id " +
                    "OR UPPER(description) LIKE UPPER(:description))",
            nativeQuery = true)
    Page<PaymentMethod> searchPaymentMethod(String id, String description, Pageable pageable);

    @Query(value = "SELECT * FROM payment_method " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<PaymentMethod> getStatus(Pageable pageable);

    boolean existsById(Long id);

}
