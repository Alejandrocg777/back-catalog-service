package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.AmountOwesSupplierDTO;
import com.asb.backCompanyService.model.ProductWarehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductWareHouseRepository extends CrudRepository<ProductWarehouse, Long> {


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.AmountOwesSupplierDTO(p.id, pr.productName, p.reservedQuantity) " +
                    "FROM ProductWarehouse p " +
                    "JOIN Product pr ON p.productId = pr.id " +
                    "WHERE p.supplierId = :supplierId ")
    Page<AmountOwesSupplierDTO> amountOwesSupplierDTO(Long supplierId, Pageable pageable);


  ProductWarehouse findByProductIdAndSupplierId(Long productId, Long supplierId);
}
