package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.SupplierDtoResponse;
import com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO;
import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.model.SupplierRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupplierRateRepository extends JpaRepository<SupplierRate, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO(sr.id, s.id, s.name, sr.rate, sr.status) " +
                    "FROM SupplierRate sr " +
                    "LEFT JOIN Supplier s ON s.id = sr.supplierId " +
                    "WHERE sr.status = 'ACTIVE' ")
    Page<SupplierRateResponseDTO> getActiveSuppliers(Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO(sr.id, s.id, s.name, sr.rate, sr.status) " +
            "FROM SupplierRate sr " +
            "INNER JOIN Supplier s ON s.id = sr.supplierId " +
            "WHERE sr.status = 'ACTIVE' " +
            "AND (CAST(sr.id AS string) LIKE :id " +
            "OR UPPER(s.name) LIKE UPPER(:supplierName) " +
            "OR CAST(sr.rate AS string) LIKE:priceRate)",
            countQuery = "SELECT COUNT(sr) " +
                    "FROM SupplierRate sr " +
                    "INNER JOIN Supplier s ON s.id = sr.supplierId " +
                    "WHERE sr.status = 'ACTIVE' " +
                    "AND (CAST(sr.id AS string) LIKE :id " +
                    "OR UPPER(s.name) LIKE UPPER(:supplierName) " +
                    "OR CAST(sr.rate AS string) LIKE:priceRate)")
    Page<SupplierRateResponseDTO> search(
            @Param("id") String id,
            @Param("supplierName") String supplierName,
            @Param("priceRate") String priceRate,
            Pageable pageable);
}
