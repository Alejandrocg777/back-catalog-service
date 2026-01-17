package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequestDTO {

    private String invoiceNumber;
    private Long billId;
    private Long customerId;
    private String address;
    private String phone;
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
    private String statusBill;
    private List<InvoiceDetailDTO> invoiceDetails;
}
