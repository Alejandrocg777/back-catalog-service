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
@Table(name = "accounting_account_group")
public class AccountingAccountGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountingAccountGroupId;

    @Column(nullable = false)
    private Integer accountingAccountGroupCode;

    @Column(nullable = false)
    private String accountingAccountGroupName;

    @ManyToOne
    @JoinColumn(name = "accounting_account_class_id", nullable = false)
    private AccountingAccountClass accountingAccountClass;

    @Column(nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'")
    private String status;
}
