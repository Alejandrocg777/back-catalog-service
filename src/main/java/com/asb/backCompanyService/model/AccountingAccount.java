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
@Table(name = "accounting_account")
public class AccountingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountingAccountId;

    @Column(nullable = false)
    private Integer accountingAccountCode;

    @Column(nullable = false)
    private String accountingAccountName;

    @ManyToOne
    @JoinColumn(name = "accounting_account_group_id", nullable = false)
    private AccountingAccountGroup accountingAccountGroup;

    @Column(nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'")
    private String status;
}
