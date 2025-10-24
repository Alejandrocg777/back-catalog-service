package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductQuantityDTO {

    private Long id;
    private Long quantity;
    private String date;
    private String observation;
    private Double purchasePrice;
    private Double total;
    private Double transactionTotal;
    private Long userId;
}
