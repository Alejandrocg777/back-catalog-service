package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_allocation_details")
@Data
public class OrderAllocationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_allocation_details_id")
    private Long id;

    @Column(name = "order_allocation_id", nullable = true)
    private Long orderAllocationId;

    @Column(name = "pending_order_detail_id", nullable = false)
    private Long pendingOrderDetailId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "assigned_quantity")
    private Integer assignedQuantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total")
    private Double total;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
