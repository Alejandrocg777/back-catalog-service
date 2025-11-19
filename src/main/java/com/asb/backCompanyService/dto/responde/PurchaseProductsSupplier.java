package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProductsSupplier {

    private Long id;
    private String productName;
    private Double purchasePrice;
    private Long quantity;
    private Double total;

}
