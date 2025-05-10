package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponseDTO {

    private Long id;

    private Double amount;

    private Long quantity;

    private String discountTypeName;

    private String status;
}
