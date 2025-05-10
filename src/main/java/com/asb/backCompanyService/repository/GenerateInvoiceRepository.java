package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.GenerateInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenerateInvoiceRepository extends JpaRepository<GenerateInvoice, Long> {


    GenerateInvoice findByResolutionNumber(String resolutionNumber);

    List<GenerateInvoice> findByAuthorizedEnabled(String authorizedEnabled);
}
