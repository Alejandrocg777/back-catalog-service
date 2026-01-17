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

    private Long id;
    private Long customerId;
    private String customerName;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private LocalDate dueDate;
    private Long paymentTypeId;
    private Long paymentMethodId;
    private String paymentMethodName;
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
    private String userName;
    private Double changeGiven;
    private String status;
    private String statusBill;
}