package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.PurchaseProductsSupplier;
import com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO;
import com.asb.backCompanyService.model.PurchaseSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PurchaseSupplierRepository extends JpaRepository<PurchaseSupplier, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO(ps.id , t.userId, ps.supplierId, s.name, t.transactionDate, ps.purchaseStatus, t.observation) " +
            "FROM PurchaseSupplier ps " +
            "JOIN Transaction t ON t.id = ps.transactionId " +
            "JOIN Supplier s ON s.id = ps.supplierId "+
            "WHERE ps.status = 'ACTIVE' ")
    Page<PurchaseSupplierResponseDTO> findAllPurchaseSuppliers(Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseProductsSupplier(p.id , p.productName, t.purchasePrice, t.quantity, t.total) " +
            "FROM PurchaseSupplier ps " +
            "JOIN TransactionProduct t ON t.transactionId = ps.transactionId " +
            "JOIN Product p ON p.id = t.productId "+
            "WHERE ps.status = 'ACTIVE' " +
            "AND ps.id =:purchaseSupplierId ")
    Page<PurchaseProductsSupplier> findAllPurchaseProducts(Pageable pageable, Long purchaseSupplierId);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO(ps.id , t.userId, ps.supplierId, s.name, t.transactionDate, ps.purchaseStatus, t.observation) " +
            "FROM PurchaseSupplier ps " +
            "JOIN Transaction t ON t.id = ps.transactionId " +
            "JOIN Supplier s ON s.id = ps.supplierId " +
            "WHERE ps.status = 'ACTIVE'")
    Page<PurchaseSupplierResponseDTO> searchPurchaseSuppliers(Pageable pageable);


    @Query("SELECT new com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO( " +
            "ps.id, t.userId, ps.supplierId, s.name, t.transactionDate, ps.purchaseStatus, t.observation) " +
            "FROM PurchaseSupplier ps " +
            "JOIN Transaction t ON t.id = ps.transactionId " +
            "JOIN Supplier s ON s.id = ps.supplierId " +
            "WHERE (:id IS NULL OR ps.id = :id) " +
            "AND (:userId IS NULL OR t.userId = :userId) " +
            "AND (:supplierId IS NULL OR ps.supplierId = :supplierId) " +
            "AND (:supplierName IS NULL OR UPPER(s.name) LIKE :supplierName) " +
            "AND (:date IS NULL OR t.transactionDate = :date) " +  // Exacto; para rango, usa >= y <= con params adicionales
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
}
