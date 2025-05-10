package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventory")
    private Long id;

    @Column(name = "nombre_equipo", nullable = false)
    private String nombreEquipo;

    @Column(name = "numero_serie", nullable = false)
    private String numeroSerie;

    @Column(name = "numero_inventario", nullable = false)
    private Long numeroInventario;

    @Column(name = "comentario", nullable = true)
    private String comentario;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_fabricante", nullable = false)
    private Long idFabricante;

    @Column(name = "id_estados_dispositivo", nullable = false)
    private Long idEstadosDispositivo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
