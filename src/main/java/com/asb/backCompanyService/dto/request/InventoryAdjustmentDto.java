package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAdjustmentDto {
    private Long id;
    private Long warehouseId;
    private BigDecimal totalAjustado;
    private String observaciones;
}
