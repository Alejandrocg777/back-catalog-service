package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierProductDtoResponse {
    private Long supplierProductId;
    private Long productId;
    private String productName;
    private BigDecimal purchasePrice;
    private String status;
}
