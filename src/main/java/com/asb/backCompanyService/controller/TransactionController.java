package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.dto.responde.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/transaction")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class TransactionController {

    private final TransactionBusiness transactionBusiness;

    /*

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(@RequestParam(defaultValue = "0") Integer page,
                                                                        @RequestParam(defaultValue = "6") Integer size,
                                                                        @RequestParam(defaultValue = "ASC") String orders,
                                                                        @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(transactionBusiness.getTransactions(page, size, orders, sortBy), HttpStatus.OK);
    }

     */

    @GetMapping
    public ResponseEntity<Page<TransactionResponseNewDTO>> getTransactions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "6") Integer size,
            @RequestParam(defaultValue = "ASC") String orders,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {

        return new ResponseEntity<>(
                transactionBusiness.getTransactions(page, size, orders, sortBy, startDate, endDate),
                HttpStatus.OK
        );
    }
    @GetMapping("/getProductsByUser/{transactionId}")
    public ResponseEntity<Page<ProductOfTransactionDTO>> getProductsOfTransaction(@RequestParam(defaultValue = "0") Integer page,
                                                                                  @RequestParam(defaultValue = "6") Integer size,
                                                                                  @RequestParam(defaultValue = "ASC") String orders,
                                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                                  @PathVariable("transactionId")Long transactionId) {
        return new ResponseEntity<>(transactionBusiness.getProductsOfTransaction(transactionId,page, size, orders, sortBy), HttpStatus.OK);
    }


    @GetMapping("/search/getProductsByUser/{transactionId}")
    public ResponseEntity<Page<ProductOfTransactionDTO>> searchProducts(@RequestParam Map<String, String> customQuery, @PathVariable("transactionId")Long transactionId) {
        Page<ProductOfTransactionDTO> products = transactionBusiness.searchProducts(transactionId, customQuery);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<TransactionResponseNewDTO>> search(
            @RequestParam Map<String, String> customQuery,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {

        Page<TransactionResponseNewDTO> transactions = transactionBusiness.searchCustom(customQuery, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

}
