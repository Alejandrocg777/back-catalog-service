package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.ClientResponseDTO;
import com.asb.backCompanyService.dto.responde.DiscountResponseDTO;
import com.asb.backCompanyService.model.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DiscountRepository extends CrudRepository<Discount, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.DiscountResponseDTO(c.id, c.amount, c.quantity, t.description, c.status) " +
            "FROM Discount c " +
            "INNER JOIN DiscountType t ON c.discountTypeId = t.id " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Discount c " +
                    "INNER JOIN DiscountType t ON c.discountTypeId = t.id " +
                    "WHERE c.status = 'ACTIVE'")
    Page<DiscountResponseDTO> getStatus(Pageable pageable);


}
