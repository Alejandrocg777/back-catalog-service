package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO;
import com.asb.backCompanyService.model.PurchaseSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseSupplierRepository extends JpaRepository<PurchaseSupplier, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO(ps.id , t.userId, ps.supplierId, s.name, t.transactionDate, ps.purchaseStatus, t.observation) " +
            "FROM PurchaseSupplier ps " +
            "JOIN Transaction t ON t.id = ps.transactionId " +
            "JOIN Supplier s ON s.id = ps.supplierId ")
    Page<PurchaseSupplierResponseDTO> findAllPurchaseSuppliers(Pageable pageable);
}
