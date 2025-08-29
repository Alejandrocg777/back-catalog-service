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

    private String description;

    private Long categoryId;

    private String categoryName;

    private Long quantity;

    private String image;

    private String status;

    private String productStatus;
}
