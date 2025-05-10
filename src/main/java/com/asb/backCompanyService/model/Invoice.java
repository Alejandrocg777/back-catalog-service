package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "cash_register_id")
    private Long cashRegisterId;

    @Column(name = "numeration_prefix_id")
    private Long numerationPrefixId;

    @Column(name = "consecutive")
    private Long consecutive;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "total")
    private Double total;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}