package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bill_details")
@Data
public class BillDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "factura_id")
    private Long facturaId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "discount_percent")
    private Double discountPercent ;

    @Column(name = "discount_fixed")
    private Double discountFixed ;

    @Column(name = "total_discount", nullable = false)
    private Double totalDiscount ;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "total", nullable = false)
    private Double total;
}
