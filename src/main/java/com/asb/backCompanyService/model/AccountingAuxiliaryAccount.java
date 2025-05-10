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
@Table(name = "accounting_auxiliary_account")
public class AccountingAuxiliaryAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountingAuxiliaryAccountId;

    @Column(nullable = false)
    private Integer accountingAuxiliaryAccountCode;

    @Column(nullable = false)
    private String accountingAuxiliaryAccountName;

    @ManyToOne
    @JoinColumn(name = "sub_accounting_account_id", nullable = false)
    private SubAccountingAccount subAccountingAccount;

    @Column(name = "status")
    private String status;
}
