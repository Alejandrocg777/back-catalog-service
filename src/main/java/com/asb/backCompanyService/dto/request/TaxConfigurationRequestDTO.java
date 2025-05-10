package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxConfigurationRequestDTO {

    private Long id;
    private Integer tax;
    private String taxCode;
    private String type;
    private String concept;
    private String status;

}
