package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.GenerateInvoiceDto;
import com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto;
import com.asb.backCompanyService.model.GenerateInvoice;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IGenerateInvoiceBusiness {

    GenerateInvoiceDto save(GenerateInvoiceDto generateInvoiceDto);

    GenerateInvoiceDto update(Long id, GenerateInvoiceDto generateInvoiceDto);

    boolean delete(Long id);

    GenerateInvoiceResponseDto get(Long id);

    List<GenerateInvoice> getAllGenerateInvoices();

    boolean setStatus(Long id, String status);


    Page<GenerateInvoiceResponseDto> getAll(int page, int size, String orders, String sortBy);

    Page<GenerateInvoice> searchGenerateInvoice(Map<String, String> customQuery);

}
