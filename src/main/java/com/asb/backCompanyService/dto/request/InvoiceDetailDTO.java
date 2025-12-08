package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailDTO {

    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double discountPercent;
    private Double discountFixed;
    private Double totalDiscount;
    private Double subtotal;
    private Double total;
}
