package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query(value = "SELECT w " +
            "FROM Warehouse w " +
            "WHERE CAST(w.id AS string) LIKE :id " +
            "OR UPPER(w.warehouseName) LIKE UPPER(:warehouseName) " +
            "OR UPPER(w.description) LIKE UPPER(:description) " +
            "OR UPPER(w.address) LIKE UPPER(:address) " +
            "OR UPPER(w.status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Warehouse w " +
                    "WHERE CAST(w.id AS string) LIKE :id " +
                    "OR UPPER(w.warehouseName) LIKE UPPER(:warehouseName) " +
                    "OR UPPER(w.description) LIKE UPPER(:description) " +
                    "OR UPPER(w.address) LIKE UPPER(:address) " +
                    "OR UPPER(w.status) LIKE UPPER(:status)")
    Page<Warehouse> searchWarehouses(String id, String warehouseName, String description, String address, String status, Pageable pageable);
}
