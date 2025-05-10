package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreliminarInvoice {

    private Long id;
    private Long cashRegister;
    private String invoiceNumber;
}
