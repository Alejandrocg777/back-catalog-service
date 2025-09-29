package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse;
import com.asb.backCompanyService.model.SupplierProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse(sp.id, sp.productId, p.productName, sp.purchasePrice, sp.status) " +
            "FROM SupplierProduct sp " +
            "JOIN Product p ON sp.productId = p.id " +
            "WHERE sp.supplierId = :supplierId " +
            "AND CAST(sp.id AS string) LIKE :supplierProductId " +
            "AND CAST(sp.productId AS string) LIKE :productId " +
            "AND UPPER(p.productName) LIKE UPPER(:productName) " +
            "AND CAST(sp.purchasePrice AS string) LIKE :purchasePrice " +
            "AND UPPER(sp.status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) " +
                    "FROM SupplierProduct sp " +
                    "JOIN Product p ON sp.productId = p.id " +
                    "WHERE sp.supplierId = :supplierId " +
                    "AND CAST(sp.id AS string) LIKE :supplierProductId " +
                    "AND CAST(sp.productId AS string) LIKE :productId " +
                    "AND UPPER(p.productName) LIKE UPPER(:productName) " +
                    "AND CAST(sp.purchasePrice AS string) LIKE :purchasePrice " +
                    "AND UPPER(sp.status) LIKE UPPER(:status)")
    Page<SupplierProductDtoResponse> searchSupplierProducts(Long supplierId, String supplierProductId, String productId, String productName, String purchasePrice, String status, Pageable pageable);

    List<SupplierProduct> findBySupplierId(Long supplierId);
}
