package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TransactionEmployeeRequestDTO {

    private Long id;

    private Long employeeId;

    private Double paymentAmount;

    private String date;

    private String observation;

    private String typeTransaction;

    private String status;
}
