package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.SupplierDtoResponse;
import com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO;
import com.asb.backCompanyService.model.SupplierRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRateRepository extends JpaRepository<SupplierRate, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO(sr.id, s.id, s.name, sr.rate, sr.status) " +
                    "FROM SupplierRate sr " +
                    "LEFT JOIN Supplier s ON s.id = sr.supplierId " +
                    "WHERE sr.status = 'ACTIVE' ")
    Page<SupplierRateResponseDTO> getActiveSuppliers(Pageable pageable);
}
