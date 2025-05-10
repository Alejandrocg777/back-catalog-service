package com.asb.backCompanyService.dto.request;

import com.asb.backCompanyService.model.AccountingAuxiliaryAccount;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountingAuxiliaryAccountDto {
    private Long id;
    private Integer code;
    private String name;
    private Long subAccountingAccountId;
    private String status;

    public AccountingAuxiliaryAccountDto(AccountingAuxiliaryAccount entity) {
        this.id = entity.getAccountingAuxiliaryAccountId();
        this.code = entity.getAccountingAuxiliaryAccountCode();
        this.name = entity.getAccountingAuxiliaryAccountName();
        this.subAccountingAccountId = entity.getSubAccountingAccount().getSubAccountingAccountId();
        this.status = entity.getStatus();
    }
}
