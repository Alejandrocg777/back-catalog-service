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
@Table(name = "sub_accounting_account")
public class SubAccountingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subAccountingAccountId;

    @Column(nullable = false)
    private Integer subAccountingAccountCode;

    @Column(nullable = false)
    private String subAccountingAccountName;

    @ManyToOne
    @JoinColumn(name = "accounting_account_id", nullable = false)
    private AccountingAccount accountingAccount;

    @Column(nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'")
    private String status;
}
