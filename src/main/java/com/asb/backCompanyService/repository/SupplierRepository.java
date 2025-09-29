package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.SupplierDtoResponse;
import com.asb.backCompanyService.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierDtoResponse(s.id, s.name, s.email, s.phone, s.categoryId, w.warehouseName, s.status) " +
            "FROM Supplier s " +
            "JOIN Warehouse w ON s.warehouseId = w.id " +
            "WHERE s.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Supplier s " +
                    "JOIN Warehouse w ON s.warehouseId = w.id " +
                    "WHERE s.status = 'ACTIVE'")
    Page<SupplierDtoResponse> getActiveSuppliers(Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.SupplierDtoResponse(s.id, s.name, s.email, s.phone, s.categoryId, w.warehouseName, s.status) " +
            "FROM Supplier s " +
            "JOIN Warehouse w ON s.warehouseId = w.id " +
            "WHERE CAST(s.id AS string) LIKE :supplierId " +
            "OR UPPER(s.name) LIKE UPPER(:name) " +
            "OR UPPER(s.email) LIKE UPPER(:email) " +
            "OR s.phone LIKE :phone " +
            "OR CAST(s.categoryId AS string) LIKE :categoryId " +
            "OR UPPER(w.warehouseName) LIKE UPPER(:warehouseName) " +
            "OR UPPER(s.status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Supplier s " +
                    "JOIN Warehouse w ON s.warehouseId = w.id " +
                    "WHERE CAST(s.id AS string) LIKE :supplierId " +
                    "OR UPPER(s.name) LIKE UPPER(:name) " +
                    "OR UPPER(s.email) LIKE UPPER(:email) " +
                    "OR s.phone LIKE :phone " +
                    "OR CAST(s.categoryId AS string) LIKE :categoryId " +
                    "OR UPPER(w.warehouseName) LIKE UPPER(:warehouseName) " +
                    "OR UPPER(s.status) LIKE UPPER(:status)")
    Page<SupplierDtoResponse> searchSuppliers(String supplierId, String name, String email, String phone, String categoryId, String warehouseName, String status, Pageable pageable);
}
