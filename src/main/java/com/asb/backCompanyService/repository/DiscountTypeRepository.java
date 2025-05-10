package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.DiscountTypeRequestDTO;
import com.asb.backCompanyService.model.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DiscountTypeRepository extends CrudRepository<DiscountType, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.request.DiscountTypeRequestDTO(c.id, c.description, c.status) " +
            "FROM DiscountType c " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM DiscountType c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<DiscountTypeRequestDTO> getStatus(Pageable pageable);

}
