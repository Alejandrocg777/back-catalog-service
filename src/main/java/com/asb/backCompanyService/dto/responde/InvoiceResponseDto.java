package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {

    private Long customerId;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private LocalDate dueDate;
    private Long paymentTypeId;
    private Long paymentMethodId;
    private String deliveryType;
    private Double deliveryCost;
    private String observations;
    private Double totalDiscount;
    private Double total;
    private Double subtotal;
    private Double initialPayment;
    private Double remainingBalance;
    private Double cashReceived;
    private Long userId;
    private Double changeGiven;
}
