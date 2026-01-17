package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingOrderDetailDto {

    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double total;

}