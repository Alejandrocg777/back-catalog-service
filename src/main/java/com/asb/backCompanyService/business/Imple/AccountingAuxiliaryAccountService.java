package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IAccountingAuxiliaryAccountBusiness;
import com.asb.backCompanyService.dto.request.AccountingAuxiliaryAccountDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.*;
import com.asb.backCompanyService.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountingAuxiliaryAccountService implements IAccountingAuxiliaryAccountBusiness {

    @Autowired
    private AccountingAuxiliaryAccountRepository auxiliaryAccountRepository;

    @Autowired
    private SubAccountingAccountRepository subAccountingAccountRepository;

    @Autowired
    private AccountingAccountRepository accountingAccountRepository;

    @Autowired
    private AccountingAccountGroupRepository accountingAccountGroupRepository;

    @Autowired
    private AccountingAccountClassRepository accountingAccountClassRepository;

    @Override
    @Transactional
    public AccountingAuxiliaryAccountDto save(AccountingAuxiliaryAccountDto dto) {

        AccountingAuxiliaryAccount entity = new AccountingAuxiliaryAccount();

        entity.setAccountingAuxiliaryAccountCode(dto.getCode());
        entity.setAccountingAuxiliaryAccountName(dto.getName());
        entity.setSubAccountingAccount(subAccountingAccountRepository.findById(dto.getSubAccountingAccountId())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Sub account not found")));
        entity.setStatus(dto.getStatus());

        auxiliaryAccountRepository.save(entity);

        return new AccountingAuxiliaryAccountDto(entity);
    }


    @Override
    @Transactional
    public GenericResponse update(Long id, AccountingAuxiliaryAccountDto dto) {
        Optional<AccountingAuxiliaryAccount> optionalEntity = auxiliaryAccountRepository.findById(id);
        if (optionalEntity.isEmpty()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Auxiliary account not found");
        }
        AccountingAuxiliaryAccount entity = optionalEntity.get();
        entity.setAccountingAuxiliaryAccountCode(dto.getCode());
        entity.setAccountingAuxiliaryAccountName(dto.getName());
        entity.setSubAccountingAccount(subAccountingAccountRepository.findById(dto.getSubAccountingAccountId())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "Sub account not found")));
        entity.setStatus(dto.getStatus());
        auxiliaryAccountRepository.save(entity);
        return new GenericResponse("Update successful", HttpStatus.OK.value());
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (auxiliaryAccountRepository.existsById(id)) {
            auxiliaryAccountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<AccountingAuxiliaryAccount> get(Long id) {
        return auxiliaryAccountRepository.findBySubAccountingAccount_SubAccountingAccountId(id);
    }

    @Override
    public List<AccountingAuxiliaryAccount> getAllAuxiliaryAccounts() {
        return auxiliaryAccountRepository.findAll();
    }

    @Override
    public List<SubAccountingAccount> getAllSubAccounts(Long accountId) {
        return subAccountingAccountRepository.findByAccountingAccount_AccountingAccountId(accountId);
    }

    @Override
    public List<AccountingAccount> getAllAccounts(Long accountGroupId) {
        return accountingAccountRepository.findByAccountingAccountGroup_AccountingAccountGroupId(accountGroupId);
    }

    @Override
    public List<AccountingAccountGroup> getAllAccountGroups(Long classId) {
        return accountingAccountGroupRepository.findByAccountingAccountClass_AccountingAccountClassId(classId);
    }

    @Override
    public List<AccountingAccountClass> getAllAccountClasses() {
        return accountingAccountClassRepository.findAll();
    }
}
