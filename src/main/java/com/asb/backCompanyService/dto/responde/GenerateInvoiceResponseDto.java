package com.asb.backCompanyService.dto.responde;

import com.asb.backCompanyService.dto.request.InvoiceDetailDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GenerateInvoiceResponseDto {

    private Long id;
    private Long customerId;
    private String customerName;
    private String address;
    private String phone;
    private String neighborhood;
    private String identification;
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
    private List<InvoiceDetailDTO> invoiceDetails = new ArrayList<>();

    public GenerateInvoiceResponseDto(Long id, Long customerId, String customerName,
                                      String address, String phone,
                                      String neighborhood, String identification,
                                      String invoiceNumber, LocalDateTime invoiceDate,
                                      LocalDate dueDate, Long paymentTypeId, Long paymentMethodId,
                                      String paymentMethodName, String deliveryType,
                                      Double deliveryCost, String observations,
                                      Double totalDiscount, Double total, Double subtotal,
                                      Double initialPayment, Double remainingBalance,
                                      Double cashReceived, Long userId, String userName,
                                      Double changeGiven, String status, String statusBill,
                                      List<InvoiceDetailDTO> invoiceDetails) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.neighborhood = neighborhood;
        this.identification = identification;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.paymentTypeId = paymentTypeId;
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
        this.deliveryType = deliveryType;
        this.deliveryCost = deliveryCost;
        this.observations = observations;
        this.totalDiscount = totalDiscount;
        this.total = total;
        this.subtotal = subtotal;
        this.initialPayment = initialPayment;
        this.remainingBalance = remainingBalance;
        this.cashReceived = cashReceived;
        this.userId = userId;
        this.userName = userName;
        this.changeGiven = changeGiven;
        this.status = status;
        this.statusBill = statusBill;
        this.invoiceDetails = invoiceDetails;
    }

    public GenerateInvoiceResponseDto(Long id, Long customerId, String customerName,
                                      String address, String phone,
                                      String neighborhood, String identification,
                                      String invoiceNumber, LocalDateTime invoiceDate,
                                      LocalDate dueDate, Long paymentTypeId, Long paymentMethodId,
                                      String paymentMethodName, String deliveryType,
                                      Double deliveryCost, String observations,
                                      Double totalDiscount, Double total, Double subtotal,
                                      Double initialPayment, Double remainingBalance,
                                      Double cashReceived, Long userId, String userName,
                                      Double changeGiven, String status, String statusBill) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.neighborhood = neighborhood;
        this.identification = identification;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.paymentTypeId = paymentTypeId;
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
        this.deliveryType = deliveryType;
        this.deliveryCost = deliveryCost;
        this.observations = observations;
        this.totalDiscount = totalDiscount;
        this.total = total;
        this.subtotal = subtotal;
        this.initialPayment = initialPayment;
        this.remainingBalance = remainingBalance;
        this.cashReceived = cashReceived;
        this.userId = userId;
        this.userName = userName;
        this.changeGiven = changeGiven;
        this.status = status;
        this.statusBill = statusBill;
        this.invoiceDetails = new ArrayList<>();
    }
}