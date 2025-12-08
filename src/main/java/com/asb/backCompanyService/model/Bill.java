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
    @Column(name = "bill_id")
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "payment_type_id", nullable = false)
    private Long paymentTypeId;

    @Column(name = "payment_method_id", nullable = false)
    private Long paymentMethodId;

    @Column(name = "delivery_type", nullable = false)
    private String deliveryType;

    @Column(name = "delivery_cost", nullable = false)
    private Double deliveryCost;

    @Column(name = "observations")
    private String observations;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "total_discount", nullable = false)
    private Double totalDiscount;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "initial_payment", nullable = false)
    private Double initialPayment;

    @Column(name = "remaining_balance", nullable = false)
    private Double remainingBalance ;

    @Column(name = "cash_received")
    private Double cashReceived;

    @Column(name = "change_given")
    private Double changeGiven;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
