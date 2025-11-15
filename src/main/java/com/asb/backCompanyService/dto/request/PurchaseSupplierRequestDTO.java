package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSupplierRequestDTO {

    private Long supplierId;
    private Long userId;
    private String date;
    private String observation;
    private Double transactionTotal;
    private String purchaseStatus;
    private List<PurchaseSupplierProductsDTO> products;
}
