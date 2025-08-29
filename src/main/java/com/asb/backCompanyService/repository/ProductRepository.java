package com.asb.backCompanyService.repository;


import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductResponseDTO(c.id, c.productName, c.price,  c.status) " +
            "FROM Product c " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Product c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<ProductResponseDTO> getStatus(Pageable pageable);
}
