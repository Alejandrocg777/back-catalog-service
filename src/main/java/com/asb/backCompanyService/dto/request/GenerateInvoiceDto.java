package com.asb.backCompanyService.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateInvoiceDto {
    private Long id;
    private String resolutionNumber;
    private String billingType;
    private String authorizedEnabled;
    private String resolutionDate;
    private String cashRegisterName;
}
