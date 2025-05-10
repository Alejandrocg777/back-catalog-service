package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.request.AccountingAuxiliaryAccountDto;
import com.asb.backCompanyService.model.*;
import java.util.List;

public interface IAccountingAuxiliaryAccountBusiness {

    AccountingAuxiliaryAccountDto save(AccountingAuxiliaryAccountDto accountingAuxiliaryAccountDto);

    GenericResponse update(Long id, AccountingAuxiliaryAccountDto accountingAuxiliaryAccountDto);

    boolean delete(Long id);

    List<AccountingAuxiliaryAccount> get(Long id);

    List<AccountingAuxiliaryAccount> getAllAuxiliaryAccounts();

    List<SubAccountingAccount> getAllSubAccounts(Long accountId);

    List<AccountingAccount> getAllAccounts(Long accountGroupId);

    List<AccountingAccountGroup> getAllAccountGroups(Long classId);

    List<AccountingAccountClass> getAllAccountClasses();
}
