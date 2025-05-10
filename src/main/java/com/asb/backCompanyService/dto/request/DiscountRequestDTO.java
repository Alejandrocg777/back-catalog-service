package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequestDTO {

    private Long id;

    private Long amount;

    private Long quantity;

    private Long discountTypeId;

    private String status;

}
