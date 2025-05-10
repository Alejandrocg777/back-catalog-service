package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query(value = "SELECT * FROM warehouse " +
            "WHERE CAST(id AS char) LIKE :id " +
            "OR UPPER(warehouse_name) LIKE UPPER(:warehouseName) " +
            "OR UPPER(email) LIKE UPPER(:email) " +
            "OR UPPER(address) LIKE UPPER(:address) " +
            "OR UPPER(description) LIKE UPPER(:description) " + // Nueva condición para el campo 'description'
            "OR UPPER(owner) LIKE UPPER(:owner)",  // Nueva condición para el campo 'owner'
            countQuery = "SELECT COUNT(*) FROM warehouse " +
                    "WHERE CAST(id AS char) LIKE :id " +
                    "OR UPPER(warehouse_name) LIKE UPPER(:warehouseName) " +
                    "OR UPPER(email) LIKE UPPER(:email) " +
                    "OR UPPER(address) LIKE UPPER(:address) " +
                    "OR UPPER(description) LIKE UPPER(:description) " + // Nueva condición para el campo 'description'
                    "OR UPPER(owner) LIKE UPPER(:owner)",  // Nueva condición para el campo 'owner'
            nativeQuery = true)
    Page<Warehouse> searchWarehouses(String id, String warehouseName, String email, String address, String description, String owner, Pageable pageable);
}
