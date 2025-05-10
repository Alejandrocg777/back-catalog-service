package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SellerRepository extends CrudRepository<Seller, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.request.SellerRequestDTO(c.id, c.name, c.lastName, c.email, c.phone, c.status) " +
            "FROM Seller c " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Seller c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<SellerRequestDTO> getStatus(Pageable pageable);


}
