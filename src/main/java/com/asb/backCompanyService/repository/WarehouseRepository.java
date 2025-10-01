package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query(value = "SELECT w " +
            "FROM Warehouse w " +
            "WHERE (CAST(w.id AS string) LIKE :id " +
            "OR UPPER(w.warehouseName) LIKE UPPER(:warehouseName) " +
            "OR UPPER(w.description) LIKE UPPER(:description) " +
            "OR UPPER(w.address) LIKE UPPER(:address)) " +
            "AND w.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Warehouse w " +
                    "WHERE (CAST(w.id AS string) LIKE :id " +
                    "OR UPPER(w.warehouseName) LIKE UPPER(:warehouseName) " +
                    "OR UPPER(w.description) LIKE UPPER(:description) " +
                    "OR UPPER(w.address) LIKE UPPER(:address)) " +
                    "AND w.status = 'ACTIVE'")
    Page<Warehouse> searchWarehouses(String id, String warehouseName, String description, String address, Pageable pageable);

    @Query(value = " select w from Warehouse w where w.status = 'ACTIVE' ")
    Page<Warehouse> getActive(Pageable pageable);

    List<Warehouse> findByStatus(String warehouseName);
}
