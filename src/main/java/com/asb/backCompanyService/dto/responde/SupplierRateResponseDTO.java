package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRateResponseDTO {

    private Long id;
    private Long supplierId;
    private String supplierName;
    private Double priceRate;
    private String status;
}
