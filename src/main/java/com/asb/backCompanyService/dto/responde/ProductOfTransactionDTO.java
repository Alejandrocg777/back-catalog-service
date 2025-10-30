package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOfTransactionDTO {

    private Long id;
    private String productName;
    private Double purchasePrice;
    private Double total;
}
