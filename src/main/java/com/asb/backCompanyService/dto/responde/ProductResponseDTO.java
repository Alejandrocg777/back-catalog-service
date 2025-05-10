package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;

    private String productName;

    private Double price;

    private Double discountAmount;

    private Long discountId;

    private Long taxConfigurationId;

    private String taxConfigurationName;

    private String status;
}
