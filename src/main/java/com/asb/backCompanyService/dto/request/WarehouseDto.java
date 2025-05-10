package com.asb.backCompanyService.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {
    private Long id;
    private String warehouseName;
    private Integer warehouseType;
    private String description; // Nueva columna
    private String owner; // Reemplazo de responsible
    private String email;
    private String address;
    private LocalDateTime createdAt; // Fecha de creación
    private LocalDateTime updatedAt; // Fecha de última actualización
}
