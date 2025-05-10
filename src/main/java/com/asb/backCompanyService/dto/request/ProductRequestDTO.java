package com.asb.backCompanyService.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private Long id;

    private String productName;

    private Double price;

    private Integer quantity;

    private Long discountId;

    private Long taxConfigurationId;

    private Double taxValue;

    private String status;

    private Double totalValue;

}
