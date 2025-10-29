package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.dto.responde.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<TransactionResponseNewDTO>> getTransactions(@RequestParam(defaultValue = "0") Integer page,
                                                                           @RequestParam(defaultValue = "6") Integer size,
                                                                           @RequestParam(defaultValue = "ASC") String orders,
                                                                           @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(transactionBusiness.getTransactions(page, size, orders, sortBy), HttpStatus.OK);
    }

    @GetMapping("/getProductsByUser/{userId}")
    public ResponseEntity<Page<ProductOfTransactionDTO>> getProductsOfTransaction(@RequestParam(defaultValue = "0") Integer page,
                                                                                  @RequestParam(defaultValue = "6") Integer size,
                                                                                  @RequestParam(defaultValue = "ASC") String orders,
                                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                                  @PathVariable("userId")Long userId) {
        return new ResponseEntity<>(transactionBusiness.getProductsOfTransaction(userId,page, size, orders, sortBy), HttpStatus.OK);
    }


    @GetMapping("/search/getProductsByUser/{userId}")
    public ResponseEntity<Page<ProductOfTransactionDTO>> searchProducts(@RequestParam Map<String, String> customQuery, @PathVariable("userId")Long userId) {
        Page<ProductOfTransactionDTO> products = transactionBusiness.searchProducts(userId, customQuery);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<TransactionResponseNewDTO>> search(@RequestParam Map<String, String> customQuery) {
        Page<TransactionResponseNewDTO> products = transactionBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(products);
    }

}
