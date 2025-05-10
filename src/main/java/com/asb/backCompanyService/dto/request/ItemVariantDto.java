package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVariantDto {
    private Long id;
    private String variantType;
    private String name;
    private String description;
    private String status;
}
