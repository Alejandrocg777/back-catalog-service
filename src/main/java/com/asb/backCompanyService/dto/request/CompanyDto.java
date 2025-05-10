package com.asb.backCompanyService.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long id;
    private String companyName;
    private String nit;
    private String address;
    private String email;
    private String phone;
    private Long economicActivityId;
    private String status;
}
