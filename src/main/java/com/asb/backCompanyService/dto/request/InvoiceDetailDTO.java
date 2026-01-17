package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailDTO {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double discountPercent;
    private Double discountFixed;
    private Double totalDiscount;
    private Double subtotal;
    private Double total;

    // Constructor SIN id y productName (para crear nuevas facturas)
    public InvoiceDetailDTO(Long productId, Integer quantity, Double unitPrice,
                            Double discountPercent, Double discountFixed,
                            Double totalDiscount, Double subtotal, Double total) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPercent = discountPercent;
        this.discountFixed = discountFixed;
        this.totalDiscount = totalDiscount;
        this.subtotal = subtotal;
        this.total = total;
    }

    // Constructor CON id pero SIN productName (para obtener facturas sin nombre de producto)
    public InvoiceDetailDTO(Long id, Long productId, Integer quantity,
                            Double unitPrice, Double discountPercent,
                            Double discountFixed, Double totalDiscount,
                            Double subtotal, Double total) {
        this.id = id;
        this.productId = productId;
        this.productName = "";
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPercent = discountPercent;
        this.discountFixed = discountFixed;
        this.totalDiscount = totalDiscount;
        this.subtotal = subtotal;
        this.total = total;
    }
}