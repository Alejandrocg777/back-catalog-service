package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.AccountingDocumentTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.AccountingDocumentType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IAccountingDocumentTypeBusiness {
    AccountingDocumentTypeDto save(AccountingDocumentTypeDto accountingDocumentTypeDto);

    GenericResponse update(Long id, AccountingDocumentTypeDto accountingDocumentTypeDto);

    boolean delete(Long id);

    AccountingDocumentTypeDto get(Long id);

    Page<AccountingDocumentType> getAll(int page, int size, String orders, String sortBy);

    Page<AccountingDocumentType> searchAccountingDocumentTypes(Map<String, String> customQuery);

    List<AccountingDocumentType> getAllAccountingDocumentTypes();
}
