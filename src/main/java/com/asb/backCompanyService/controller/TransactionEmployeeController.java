package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.business.Interfaces.TransactionEmployeeBusiness;
import com.asb.backCompanyService.dto.request.TransactionEmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO;
import com.asb.backCompanyService.dto.responde.TransactionEmployeeResponseDTO;
import com.asb.backCompanyService.dto.responde.TransactionResponseNewDTO;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/transaction-employee")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class TransactionEmployeeController {

    private final TransactionEmployeeBusiness transactionEmployeeBusiness;


    @PostMapping("/create")
    public ResponseEntity<TransactionEmployeeResponseDTO> createTransaction(
            @RequestBody TransactionEmployeeRequestDTO requestDTO) {

        log.info("Iniciando creación de transacción para empleado ID: {}", requestDTO.getEmployeeId());

        TransactionEmployeeResponseDTO response = transactionEmployeeBusiness.createTransaction(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Page<TransactionEmployeeResponseDTO>> getTransactions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "6") Integer size,
            @RequestParam(defaultValue = "ASC") String orders,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {

        return new ResponseEntity<>(
                transactionEmployeeBusiness.getTransactions(page, size, orders, sortBy, startDate, endDate),
                HttpStatus.OK
        );
    }


    @GetMapping("/search")
    public ResponseEntity<Page<TransactionEmployeeResponseDTO>> search(
            @RequestParam Map<String, String> customQuery,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {

        Page<TransactionEmployeeResponseDTO> products = transactionEmployeeBusiness.search(customQuery, startDate, endDate);
        return ResponseEntity.ok(products);
    }
}
