package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellProductResponseWithTotal {

    private Long productId;
    private Long invoiceId;
    private String product;
    private String price;
    private Long quantity;
    private Long discountAmount;
    private Long tax;
    private Double priceDiscount;
    private Double priceTax;
}
