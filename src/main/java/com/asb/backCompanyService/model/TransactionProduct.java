package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_product")
public class TransactionProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_product_id")
    private Long id;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "total")
    private Double total;

}
