package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.CashRegisterRequestDTO;
import com.asb.backCompanyService.model.CashRegister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CashRegisterRepository extends CrudRepository<CashRegister, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.request.CashRegisterRequestDTO(c.id, c.cashRegisterCode, c.description, c.status) " +
            "FROM CashRegister c " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM CashRegister c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<CashRegisterRequestDTO> getStatus(Pageable pageable);
}
