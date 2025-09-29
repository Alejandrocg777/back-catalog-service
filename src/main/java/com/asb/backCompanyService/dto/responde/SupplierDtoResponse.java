package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDtoResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Long categoryId;
    private String warehouseName;
    private String status;
}
