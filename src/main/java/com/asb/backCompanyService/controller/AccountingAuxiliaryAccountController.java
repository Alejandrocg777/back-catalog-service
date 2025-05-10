package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IAccountingAuxiliaryAccountBusiness;
import com.asb.backCompanyService.dto.request.AccountingAuxiliaryAccountDto;
import com.asb.backCompanyService.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/accounting")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class AccountingAuxiliaryAccountController {

    private final IAccountingAuxiliaryAccountBusiness business;

    @Autowired
    public AccountingAuxiliaryAccountController(IAccountingAuxiliaryAccountBusiness business) {
        this.business = business;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountingAuxiliaryAccountDto> createAuxiliaryAccount(@RequestBody AccountingAuxiliaryAccountDto dto) {
        return ResponseEntity.ok(business.save(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAuxiliaryAccount(@PathVariable Long id, @RequestBody AccountingAuxiliaryAccountDto dto) {
        return ResponseEntity.ok(business.update(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAuxiliaryAccount(@PathVariable Long id) {
        if (business.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-auxiliary-accounts/{id}")
    public ResponseEntity<List<AccountingAuxiliaryAccount>> getAuxiliaryAccount(@PathVariable Long id) {
        return ResponseEntity.ok(business.get(id));
    }

    @GetMapping("/get-all-auxiliary-accounts")
    public ResponseEntity<List<AccountingAuxiliaryAccount>> getAllAuxiliaryAccounts() {
        return ResponseEntity.ok(business.getAllAuxiliaryAccounts());
    }

    @GetMapping("/get-all-sub-accounts-by-account-id/{accountId}")
    public ResponseEntity<List<SubAccountingAccount>> getAllSubAccounts(@PathVariable("accountId")Long accountId) {
        return ResponseEntity.ok(business.getAllSubAccounts(accountId));
    }

    @GetMapping("/get-all-accounts-by-account-group-id/{accountGroupId}")
    public ResponseEntity<List<AccountingAccount>> getAllAccounts(@PathVariable("accountGroupId") Long accountGroupId) {
        return ResponseEntity.ok(business.getAllAccounts(accountGroupId));
    }

    @GetMapping("/get-all-account-groups-by-class-id/{classId}")
    public ResponseEntity<List<AccountingAccountGroup>> getAllAccountGroups(@PathVariable("classId")Long classId) {
        return ResponseEntity.ok(business.getAllAccountGroups(classId));
    }

    @GetMapping("/get-all-account-classes")
    public ResponseEntity<List<AccountingAccountClass>> getAllAccountClasses() {
        return ResponseEntity.ok(business.getAllAccountClasses());
    }
}
