package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSupplierProductsDTO {

    private Long productId;
    private Double purchasePrice;
    private Long quantity;
    private Double total;
}
