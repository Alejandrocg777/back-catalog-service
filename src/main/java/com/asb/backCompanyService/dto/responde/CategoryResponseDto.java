package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String nameCategory;
    private Double soldOutValue;
    private Double fewUnits;
    private String status;
    private String image;
}
