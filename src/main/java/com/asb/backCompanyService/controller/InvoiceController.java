package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IInvoiceBusiness;
import com.asb.backCompanyService.dto.SellProducts;
import com.asb.backCompanyService.dto.request.DeleteSellProductRequstDto;
import com.asb.backCompanyService.dto.request.InvoiceDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.InvoiceResponseDto;
import com.asb.backCompanyService.dto.responde.PreliminarInvoice;
import com.asb.backCompanyService.dto.responde.SellProductResponseDTO;
import com.asb.backCompanyService.model.Company;
import com.asb.backCompanyService.model.Invoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/invoice")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class InvoiceController {

    private final IInvoiceBusiness iInvoiceBusiness;



    @GetMapping("/get/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDto invoiceResponse = iInvoiceBusiness.get(id);
        return ResponseEntity.ok(invoiceResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<PreliminarInvoice> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        PreliminarInvoice createdInvoice = iInvoiceBusiness.save(invoiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }


    @PostMapping("/save")
    public ResponseEntity<InvoiceDto> saveInvoice(@RequestBody InvoiceDto invoiceDto) {
        InvoiceDto createdInvoice = iInvoiceBusiness.saveTotal(invoiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    @PostMapping("/saveProduct")
    public ResponseEntity<GenericResponse> saveProducts(@RequestBody SellProducts product) {
        GenericResponse productSaved = iInvoiceBusiness.saveProducts(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<Void> deleteProducts(@RequestBody DeleteSellProductRequstDto delete) {
        iInvoiceBusiness.deleteProduct(delete);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDto invoiceDto) {
        InvoiceDto updatedInvoice = iInvoiceBusiness.update(id, invoiceDto);
        return ResponseEntity.ok(updatedInvoice);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        iInvoiceBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/set-status/{id}")
    public ResponseEntity<Void> setStatus(@PathVariable("id") Long id, @RequestParam String status) {
        boolean updated = iInvoiceBusiness.setStatus(id, status);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Invoice>> search(@RequestParam Map<String, String> customQuery) {
        Page<Invoice> invoicePage = iInvoiceBusiness.searchInvoice(customQuery);
        return ResponseEntity.ok(invoicePage);
    }

    @GetMapping("/no-page/getAllInvoice")
    public ResponseEntity<List<Invoice>> getAll() {
        List<Invoice> invoices = iInvoiceBusiness.getAllInvoice();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<SellProductResponseDTO> getAllProducts(){
        return ResponseEntity.ok(iInvoiceBusiness.getAllProducts());
    }
}
