package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse;
import com.asb.backCompanyService.model.SupplierProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse(sp.id, sp.productId, p.productName, sp.purchasePrice, sp.status) " +
            "FROM SupplierProduct sp " +
            "JOIN Product p ON sp.productId = p.id " +
            "WHERE sp.supplierId = :supplierId " +
            "AND sp.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM SupplierProduct sp " +
                    "JOIN Product p ON sp.productId = p.id " +
                    "WHERE sp.supplierId = :supplierId " +
                    "AND sp.status = 'ACTIVE'")
    Page<SupplierProductDtoResponse> getActiveProductsBySupplier(Long supplierId, Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse(" +
            "sp.id, sp.productId, p.productName, sp.purchasePrice, sp.status) " +
            "FROM SupplierProduct sp " +
            "JOIN Product p ON sp.productId = p.id " +
            "WHERE sp.supplierId = :supplierId " +
            "AND (:id IS NULL OR CAST(sp.id AS string) LIKE :id) " +
            "AND (:productName IS NULL OR UPPER(p.productName) LIKE :productName) " +
            "AND (:purchasePrice IS NULL OR CAST(sp.purchasePrice AS string) LIKE :purchasePrice) " +
            "AND (:status IS NULL OR UPPER(sp.status) LIKE :status)",
            countQuery = "SELECT COUNT(sp) " +
                    "FROM SupplierProduct sp " +
                    "JOIN Product p ON sp.productId = p.id " +
                    "WHERE sp.supplierId = :supplierId " +
                    "AND (:id IS NULL OR CAST(sp.id AS string) LIKE :id) " +
                    "AND (:productName IS NULL OR UPPER(p.productName) LIKE :productName) " +
                    "AND (:purchasePrice IS NULL OR CAST(sp.purchasePrice AS string) LIKE :purchasePrice) " +
                    "AND (:status IS NULL OR UPPER(sp.status) LIKE :status)")
    Page<SupplierProductDtoResponse> searchSupplierProducts(
            @Param("supplierId") Long supplierId,
            @Param("id") String id,
            @Param("productName") String productName,
            @Param("purchasePrice") String purchasePrice,
            @Param("status") String status,
            Pageable pageable);

    List<SupplierProduct> findBySupplierId(Long supplierId);

    Boolean existsByProductId(Long productId);

    SupplierProduct findBySupplierIdAndProductId(Long supplierId, Long productId);

    @Query(value = "SELECT sp.purchasePrice FROM SupplierProduct sp WHERE sp.supplierId = :supplierId AND sp.productId = :productId ")
    BigDecimal findPurchasePriceBySupplierIdAndProductId(Long supplierId, Long productId);

}
