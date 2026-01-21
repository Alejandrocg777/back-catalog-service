package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pending_order")
@Data
public class PendingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pending_order_id")
    private Long id;

    @Column(name = "bill_id", nullable = true)
    private Long billId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "observation")
    private String observations;

    @Column(name = "date_pending_order")
    private LocalDate date;

    @Column(name = "total")
    private Double total;

    @Column(name = "status")
    private String status;

    @Column(name = "status_pending_order")
    private String statusPendingOrder;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
