package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tax_liability")
public class TaxLiability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_liability_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

}
