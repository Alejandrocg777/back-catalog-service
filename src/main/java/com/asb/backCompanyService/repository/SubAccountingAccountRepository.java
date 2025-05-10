package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.SubAccountingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubAccountingAccountRepository extends JpaRepository<SubAccountingAccount, Long> {
    List<SubAccountingAccount> findByAccountingAccount_AccountingAccountId(Long accountingAccountId);
}
