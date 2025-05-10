package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IGenerateInvoiceBusiness;
import com.asb.backCompanyService.dto.request.GenerateInvoiceDto;
import com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto;
import com.asb.backCompanyService.model.GenerateInvoice;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/generate-invoice")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class GenerateInvoiceController {

    private final IGenerateInvoiceBusiness iGenerateInvoiceBusiness;


    @GetMapping("/all")
    public ResponseEntity<List<GenerateInvoice>> getAllGenerateInvoices() {
        List<GenerateInvoice> generateInvoices = iGenerateInvoiceBusiness.getAllGenerateInvoices();
        return ResponseEntity.ok(generateInvoices);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<GenerateInvoiceResponseDto> getGenerateInvoiceById(@PathVariable Long id) {
        GenerateInvoiceResponseDto generateInvoiceResponse = iGenerateInvoiceBusiness.get(id);
        return ResponseEntity.ok(generateInvoiceResponse);
    }


    @PostMapping("/create")
    public ResponseEntity<GenerateInvoiceDto> createGenerateInvoice(@RequestBody GenerateInvoiceDto generateInvoiceDto) {
        GenerateInvoiceDto createdGenerateInvoice = iGenerateInvoiceBusiness.save(generateInvoiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenerateInvoice);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<GenerateInvoiceDto> updateGenerateInvoice(@PathVariable Long id, @RequestBody GenerateInvoiceDto generateInvoiceDto) {
        GenerateInvoiceDto updatedGenerateInvoice = iGenerateInvoiceBusiness.update(id, generateInvoiceDto);
        return ResponseEntity.ok(updatedGenerateInvoice);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGenerateInvoice(@PathVariable Long id) {
        iGenerateInvoiceBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/set-status/{id}")
    public ResponseEntity<Void> setStatus(@PathVariable("id") Long id, @RequestParam String status) {
        boolean updated = iGenerateInvoiceBusiness.setStatus(id, status);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<Page<GenerateInvoiceResponseDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size,
                                                                   @RequestParam(defaultValue = "ASC") String orders,
                                                                   @RequestParam(defaultValue = "generate_invoice_id") String sortBy) {
        Page<GenerateInvoiceResponseDto> generateInvoices = iGenerateInvoiceBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(generateInvoices);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<GenerateInvoice>> search(@RequestParam Map<String, String> customQuery) {
        Page<GenerateInvoice> generateInvoicePage = iGenerateInvoiceBusiness.searchGenerateInvoice(customQuery);
        return ResponseEntity.ok(generateInvoicePage);
    }


    @GetMapping("/no-page/getAllGenerateInvoices")
    public ResponseEntity<List<GenerateInvoice>> getAllGenerateInvoicesNoPage() {
        List<GenerateInvoice> generateInvoices = iGenerateInvoiceBusiness.getAllGenerateInvoices();
        return ResponseEntity.ok(generateInvoices);
    }
}
