package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id", nullable = true)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "payment_type_id")
    private Long paymentTypeId;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "delivery_type")
    private String deliveryType;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "delivery_cost")
    private Double deliveryCost;

    @Column(name = "observations")
    private String observations;

    @Column(name = "status")
    private String status;

    @Column(name = "status_bill")
    private String statusBill;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "total_discount")
    private Double totalDiscount;

    @Column(name = "total")
    private Double total;

    @Column(name = "initial_payment")
    private Double initialPayment;

    @Column(name = "remaining_balance")
    private Double remainingBalance ;

    @Column(name = "cash_received")
    private Double cashReceived;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "change_given")
    private Double changeGiven;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
