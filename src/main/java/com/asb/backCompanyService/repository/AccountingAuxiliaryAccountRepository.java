package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.AccountingAuxiliaryAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountingAuxiliaryAccountRepository extends JpaRepository<AccountingAuxiliaryAccount, Long> {
    List<AccountingAuxiliaryAccount> findBySubAccountingAccount_SubAccountingAccountId(Long subAccountingAccountId);
}
