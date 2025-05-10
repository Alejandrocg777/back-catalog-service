package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.MethodPaymentRequestDTO;
import com.asb.backCompanyService.model.MethodPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MethodPaymentRepository extends CrudRepository<MethodPayment, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.request.MethodPaymentRequestDTO(c.id, c.type, c.code, c.status) " +
            "FROM MethodPayment c " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM MethodPayment c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<MethodPaymentRequestDTO> getStatus(Pageable pageable);
}
