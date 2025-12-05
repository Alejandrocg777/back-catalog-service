package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_rate_id")
    private Long id;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "status")
    private String status;
}
