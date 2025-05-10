package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {
    private Long id;
    private String companyName;
    private String nit;
    private String address;
    private String email;
    private String phone;
    private String description;
    private String ciiuCode;
    private Long economicActivityId;
}
