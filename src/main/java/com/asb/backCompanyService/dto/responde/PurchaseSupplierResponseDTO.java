package com.asb.backCompanyService.dto.responde;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSupplierResponseDTO {

    private Long id;
    private Long userId;
    private Long supplierId;
    private String supplierName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime date;
    private String purchaseStatus;
    private String status;

}
