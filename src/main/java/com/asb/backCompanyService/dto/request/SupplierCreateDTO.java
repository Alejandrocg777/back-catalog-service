package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierCreateDTO {

    private String name;
    private String email;
    private String phone;
    private Long categoryId;
    private Long warehouseId;
}
