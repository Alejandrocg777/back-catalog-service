package com.asb.backCompanyService.dto.request;

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

    private String description;

    private Long categoryId;

    private Long quantity;

     private String image;

    private String status;

    private String statusProduct;

    private Double purchasePrice;

}
