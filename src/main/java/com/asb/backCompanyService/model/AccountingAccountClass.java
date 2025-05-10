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
@Table(name = "accounting_account_class")
public class AccountingAccountClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountingAccountClassId;

    @Column(nullable = false)
    private Integer accountingAccountClassCode;

    @Column(nullable = false)
    private String accountingAccountClassName;

    @Column(nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'")
    private String status;
}
