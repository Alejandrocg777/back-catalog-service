package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.PendingOrderDetailDto;
import com.asb.backCompanyService.dto.responde.PendingOrderProductDtoResponse;
import com.asb.backCompanyService.dto.responde.PurchaseProductsSupplier;
import com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO;
import com.asb.backCompanyService.model.PurchaseSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseSupplierRepository extends JpaRepository<PurchaseSupplier, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO(ps.id , t.userId, ps.supplierId, s.name, t.transactionDate, ps.purchaseStatus, t.observation) " +
            "FROM PurchaseSupplier ps " +
            "JOIN Transaction t ON t.id = ps.transactionId " +
            "JOIN Supplier s ON s.id = ps.supplierId "+
            "WHERE ps.status = 'ACTIVE' ")
    Page<PurchaseSupplierResponseDTO> findAllPurchaseSuppliers(Pageable pageable);


    @Query("SELECT new com.asb.backCompanyService.dto.responde.PurchaseProductsSupplier(p.id , p.productName, t.purchasePrice, t.quantity, t.total) " +
            "FROM TransactionProduct t " +
            "INNER JOIN Product p ON t.productId = p.id " +
            "WHERE t.transactionId = :transactionId")
    List<PurchaseProductsSupplier> findAllPurchaseProducts(@Param("transactionId") Long transactionId);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseProductsSupplier(" +
            "p.id, p.productName, t.purchasePrice, t.quantity, t.total) " +
            "FROM TransactionProduct t " +
            "INNER JOIN Product p ON t.productId = p.id " +
            "INNER JOIN PurchaseSupplier ps ON t.transactionId = ps.transactionId " +
            "WHERE ps.id = :purchaseSupplierId " +
            "AND p.status = 'ACTIVE'")
    Page<PurchaseProductsSupplier> findAllPurchaseProducts(
            @Param("purchaseSupplierId") Long purchaseSupplierId,
            Pageable pageable
    );

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO(ps.id , t.userId, ps.supplierId, s.name, t.transactionDate, ps.purchaseStatus, t.observation) " +
            "FROM PurchaseSupplier ps " +
            "JOIN Transaction t ON t.id = ps.transactionId " +
            "JOIN Supplier s ON s.id = ps.supplierId " +
            "WHERE ps.status = 'ACTIVE'")
    Page<PurchaseSupplierResponseDTO> searchPurchaseSuppliers(Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO( " +
            "ps.id, t.userId, ps.supplierId, s.name, t.transactionDate, ps.purchaseStatus, t.observation) " +
            "FROM PurchaseSupplier ps " +
            "JOIN Transaction t ON t.id = ps.transactionId " +
            "JOIN Supplier s ON s.id = ps.supplierId " +
            "WHERE ps.status = 'ACTIVE' " +
            "AND (:id IS NULL OR ps.id = :id) " +
            "AND (:userId IS NULL OR t.userId = :userId) " +
            "AND (:supplierId IS NULL OR ps.supplierId = :supplierId) " +
            "AND (:supplierName IS NULL OR UPPER(s.name) LIKE :supplierName) " +
            "AND (FUNCTION('DATE', t.transactionDate) = COALESCE(FUNCTION('DATE', :date), FUNCTION('DATE', t.transactionDate))) " +
            "AND (:purchaseStatus IS NULL OR UPPER(ps.purchaseStatus) LIKE :purchaseStatus) " +
            "AND (:observation IS NULL OR UPPER(t.observation) LIKE :observation)",
            countQuery = "SELECT COUNT(ps) " +
                    "FROM PurchaseSupplier ps " +
                    "JOIN Transaction t ON t.id = ps.transactionId " +
                    "JOIN Supplier s ON s.id = ps.supplierId " +
                    "WHERE ps.status = 'ACTIVE' " +
                    "AND (:id IS NULL OR ps.id = :id) " +
                    "AND (:userId IS NULL OR t.userId = :userId) " +
                    "AND (:supplierId IS NULL OR ps.supplierId = :supplierId) " +
                    "AND (:supplierName IS NULL OR UPPER(s.name) LIKE :supplierName) " +
                    "AND (FUNCTION('DATE', t.transactionDate) = COALESCE(FUNCTION('DATE', :date), FUNCTION('DATE', t.transactionDate))) " +
                    "AND (:purchaseStatus IS NULL OR UPPER(ps.purchaseStatus) LIKE :purchaseStatus) " +
                    "AND (:observation IS NULL OR UPPER(t.observation) LIKE :observation)")
    Page<PurchaseSupplierResponseDTO> searchPurchaseSuppliers(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("supplierId") Long supplierId,
            @Param("supplierName") String supplierName,
            @Param("date") LocalDateTime date,
            @Param("purchaseStatus") String purchaseStatus,
            @Param("observation") String observation,
            Pageable pageable
    );

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseProductsSupplier(" +
            "op.id,  p.productName,  op.purchasePrice, op.quantity, op.total) " +
            "FROM TransactionProduct op " +
            "JOIN Product p ON op.productId = p.id " +
            "WHERE op.transactionId = :purchaseSupplierId " +
            "AND p.status = 'ACTIVE' " +
            "AND (CAST(op.id AS string) LIKE :id " +
            "OR UPPER(p.productName) LIKE UPPER(:productName) " +
            "OR CAST(op.quantity AS string) LIKE :quantity " +
            "OR CAST(op.purchasePrice AS string) LIKE :purchasePrice " +
            "OR CAST(op.total AS string) LIKE :total)",
            countQuery = "SELECT COUNT(op) " +
                    "FROM TransactionProduct op " +
                    "JOIN Product p ON op.productId = p.id " +
                    "WHERE op.transactionId = :purchaseSupplierId " +
                    "AND p.status = 'ACTIVE' " +
                    "AND (CAST(op.id AS string) LIKE :id " +
                    "OR UPPER(p.productName) LIKE UPPER(:productName) " +
                    "OR CAST(op.quantity AS string) LIKE :quantity " +
                    "OR CAST(op.purchasePrice AS string) LIKE :purchasePrice " +
                    "OR CAST(op.total AS string) LIKE :total)")
    Page<PurchaseProductsSupplier> searchProductByPurchase(
            @Param("purchaseSupplierId") Long purchaseSupplierId,
            @Param("id") String id,
            @Param("productName") String productName,
            @Param("purchasePrice") String purchasePrice,
            @Param("quantity") String quantity,
            @Param("total") String total,
            Pageable pageable);


    Page<PurchaseSupplier> findAll(Specification<PurchaseSupplier> spec, Pageable pagingSort);
}
