package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.AmountOwesSupplierDTO;
import com.asb.backCompanyService.dto.responde.PurchaseProductsSupplier;
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


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.AmountOwesSupplierDTO(" +
            "op.id,  p.productName,  op.reservedQuantity) " +
            "FROM ProductWarehouse op " +
            "JOIN Product p ON op.productId = p.id " +
            "WHERE op.supplierId = :supplierId " +
            "AND p.status = 'ACTIVE' " +
            "AND (CAST(op.id AS string) LIKE :id " +
            "OR UPPER(p.productName) LIKE UPPER(:productName) " +
            "OR CAST(op.reservedQuantity AS string) LIKE :remainingAmount)",
            countQuery = "SELECT COUNT(op) " +
                    "FROM ProductWarehouse op " +
                    "JOIN Product p ON op.productId = p.id " +
                    "WHERE op.supplierId = :supplierId " +
                    "AND p.status = 'ACTIVE' " +
                    "AND (CAST(op.id AS string) LIKE :id " +
                    "OR UPPER(p.productName) LIKE UPPER(:productName) " +
                    "OR CAST(op.reservedQuantity AS string) LIKE :remainingAmount)")
    Page<AmountOwesSupplierDTO> searchProductByAmountThatSupplierOwes(
            @Param("supplierId") Long purchaseSupplierId,
            @Param("id") String id,
            @Param("productName") String productName,
            @Param("remainingAmount") String remainingAmount,
            Pageable pageable);



    ProductWarehouse findByProductIdAndSupplierId(Long productId, Long supplierId);
}
