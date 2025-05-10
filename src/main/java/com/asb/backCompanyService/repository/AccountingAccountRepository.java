package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.AccountingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountingAccountRepository extends JpaRepository<AccountingAccount, Long> {
    List<AccountingAccount> findByAccountingAccountGroup_AccountingAccountGroupId(Long accountingAccountGroupId);
}
