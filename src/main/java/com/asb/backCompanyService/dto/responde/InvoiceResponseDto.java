package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {
    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private Long paymentMethodId;
    private Double total;
    private String status;
    private String createdAt;
    private String updatedAt;
}
