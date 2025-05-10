package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.WarehouseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseTypeRepository extends JpaRepository<WarehouseType, Long> {
    // Ya heredas el método findAll() de JpaRepository para obtener todos los registros sin paginación

    @Query(value = "SELECT * FROM warehouse_types " +
            "WHERE CAST(id AS char) LIKE :id " +
            "OR UPPER(description) LIKE UPPER(:description) " +
            "OR UPPER(status) LIKE UPPER(:status) ",
            countQuery = "SELECT COUNT(*) FROM warehouse_types " +
                    "WHERE CAST(id AS char) LIKE :id " +
                    "OR UPPER(description) LIKE UPPER(:description) " +
                    "OR UPPER(status) LIKE UPPER(:status) ",
            nativeQuery = true)
    Page<WarehouseType> searchWarehouseTypes(String id, String description, String status, Pageable pageable);
}