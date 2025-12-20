package com.asb.backCompanyService.dto.responde;

import com.asb.backCompanyService.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEmployeeResponseDTO {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private Double paymentAmount;

    private String date;

    private String observation;

    private TransactionType typeTransaction;

    private String status;

}
