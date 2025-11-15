package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "purchase_supplier")
public class PurchaseSupplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_supplier_id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "purchase_status")
    private String purchaseStatus;

}
