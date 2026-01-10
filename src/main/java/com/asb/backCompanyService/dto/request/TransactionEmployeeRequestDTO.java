package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TransactionEmployeeRequestDTO {

    private Long id;

    private Long employeeId;

    private Double paymentAmount;

    private LocalDate date;

    private String observation;

    private String typeTransaction;

    private String status;
}
