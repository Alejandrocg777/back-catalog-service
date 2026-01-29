package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pending_order_detail")
@Data
public class PendingOrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pending_order_detail_id")
    private Long id;

    @Column(name = "pending_order_id")
    private Long pendingOrderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "discount", nullable = true)
    private Double discount;
}
