package com.asb.backCompanyService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellProducts {

    private Long invoiceId;
    private Long productId;
    private Long quantity;
}
