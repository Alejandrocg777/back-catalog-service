package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuppliersWhoMustDTO {

    private Long id;
    private String supplierName;
    private String warehouseName;
    private String purchaseStatus;
    private String status;
}
