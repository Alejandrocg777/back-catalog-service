package com.asb.backCompanyService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "generate_invoice")
public class GenerateInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "generate_invoice_id")
    private Long id;

    @Column(name = "resolution_number", nullable = false)
    private String resolutionNumber;

    @Column(name = "billing_type", nullable = false)
    private String billingType;

    @Column(name = "authorized_enabled", nullable = false)
    private String authorizedEnabled;

    @Column(name = "resolution_date", nullable = false)
    private String resolutionDate;

    @Column(name = "cash_register_name", nullable = false)
    private String cashRegisterName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
