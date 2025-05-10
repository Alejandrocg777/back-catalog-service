package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.AccountingAccountGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountingAccountGroupRepository extends JpaRepository<AccountingAccountGroup, Long> {
    List<AccountingAccountGroup>findByAccountingAccountClass_AccountingAccountClassId(Long accountingAccountClassId);
}
