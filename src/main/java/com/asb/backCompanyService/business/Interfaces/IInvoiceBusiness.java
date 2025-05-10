package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.SellProducts;
import com.asb.backCompanyService.dto.request.DeleteSellProductRequstDto;
import com.asb.backCompanyService.dto.request.InvoiceDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.InvoiceResponseDto;
import com.asb.backCompanyService.dto.responde.PreliminarInvoice;
import com.asb.backCompanyService.dto.responde.SellProductResponseDTO;
import com.asb.backCompanyService.model.Invoice;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IInvoiceBusiness {

    PreliminarInvoice save(InvoiceDto invoiceDto);

    InvoiceDto saveTotal(InvoiceDto invoiceDto);

    InvoiceDto update(Long id, InvoiceDto invoiceDto);

    boolean delete(Long id);

    InvoiceResponseDto get(Long id);

    List<Invoice> getAllInvoice();

    boolean setStatus(Long id, String status);

    Page<Invoice> searchInvoice(Map<String, String> customQuery);

    GenericResponse saveProducts(SellProducts product);

    void deleteProduct(DeleteSellProductRequstDto delete);

    SellProductResponseDTO getAllProducts();
}
