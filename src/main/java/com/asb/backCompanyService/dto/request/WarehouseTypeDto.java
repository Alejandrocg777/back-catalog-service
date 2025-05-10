package com.asb.backCompanyService.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseTypeDto {
    private Long id;
    private String description;
    private String status;
    private LocalDateTime createdAt; // Fecha de creación
    private LocalDateTime updatedAt; // Fecha de última actualización
}
