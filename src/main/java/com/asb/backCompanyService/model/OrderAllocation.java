package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_allocation")
@Data
public class OrderAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_allocation_id")
    private Long id;

    @Column(name = "transporter_id", nullable = true)
    private Long transporterId;

    @Column(name = "pending_order_id", nullable = true)
    private Long pendingOrderId;

    @Column(name = "origin_warehouse_id", nullable = false)
    private Long originWarehouseId;

    @Column(name = "destination_neighborhood_id", nullable = false)
    private Long destinationNeighborhoodId;

    @Column(name = "delivery_date")
    private LocalDate date;

    @Column(name = "delivery_time")
    private LocalTime hour;

    @Column(name = "charge_invoice")
    private Boolean chargeInvoice;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "observation")
    private String observations;

    @Column(name = "total_amount")
    private Double total;

    @Column(name = "status")
    private String status;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "signature",nullable = true)
    private String signature;

    @Column(name = "status_order_allocation")
    private String statusOrderAllocation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
