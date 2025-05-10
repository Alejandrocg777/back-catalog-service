package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GenerateInvoiceResponseDto {
    private Long id;
    private String resolutionNumber;
    private String billingType;
    private String authorizedEnabled;
    private String resolutionDate;
    private String cashRegisterName;
}
