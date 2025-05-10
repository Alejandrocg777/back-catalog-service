package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.InventoryAdjustment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustment, Long> {

    @Query(value = "SELECT * FROM inventory_adjustment " +
            "WHERE CAST(id AS char) LIKE :id " +
            "OR CAST(warehouse_id AS char) LIKE :warehouseId " +
            "OR UPPER(observaciones) LIKE UPPER(:observaciones) " +
            "OR total_ajustado LIKE :totalAjustado",
            countQuery = "SELECT COUNT(*) FROM inventory_adjustment " +
                    "WHERE CAST(id AS char) LIKE :id " +
                    "OR CAST(warehouse_id AS char) LIKE :warehouseId " +
                    "OR UPPER(observaciones) LIKE UPPER(:observaciones) " +
                    "OR total_ajustado LIKE :totalAjustado",
            nativeQuery = true)
    Page<InventoryAdjustment> searchInventoryAdjustment(String id, String warehouseId, String observaciones, String totalAjustado, Pageable pageable);

    @Query(value = "SELECT * FROM inventory_adjustment WHERE total_ajustado > 0", nativeQuery = true)
    Page<InventoryAdjustment> getPositiveAdjustments(Pageable pageable);

    Page<InventoryAdjustment> findByWarehouseIdAndObservacionesContainingIgnoreCase(Long warehouseId, String observaciones, Pageable pageable);
}
