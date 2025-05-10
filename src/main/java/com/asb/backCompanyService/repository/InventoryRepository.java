package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query(value = "SELECT * FROM inventory " +
            "WHERE CAST(id_inventory AS char) LIKE :id " +
            "OR UPPER(nombre_equipo) LIKE UPPER(:nombreEquipo) " +
            "OR UPPER(numero_serie) LIKE UPPER(:numeroSerie) " +
            "OR CAST(numero_inventario AS char) LIKE :numeroInventario " +
            "OR UPPER(comentario) LIKE UPPER(:comentario) " +
            "OR UPPER(status) LIKE UPPER(:status) ",
            countQuery = "SELECT COUNT(*) FROM inventory " +
                    "WHERE CAST(id AS char) LIKE :id " +
                    "OR UPPER(nombre_equipo) LIKE UPPER(:nombreEquipo) " +
                    "OR UPPER(numero_serie) LIKE UPPER(:numeroSerie) " +
                    "OR CAST(numero_inventario AS char) LIKE :numeroInventario " +
                    "OR UPPER(comentario) LIKE UPPER(:comentario) " +
                    "OR UPPER(status) LIKE UPPER(:status) ",
            nativeQuery = true)
    Page<Inventory> searchInventory(String id, String nombreEquipo, String numeroSerie, String numeroInventario, String comentario, String status, Pageable pageable);

    @Query(value = "SELECT * FROM inventory " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<Inventory> getActiveInventory(Pageable pageable);

    boolean existsById(Long id);
}
