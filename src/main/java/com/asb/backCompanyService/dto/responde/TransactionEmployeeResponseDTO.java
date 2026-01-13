package com.asb.backCompanyService.dto.responde;

import com.asb.backCompanyService.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEmployeeResponseDTO {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private Double paymentAmount;

    private LocalDate date;

    private String observation;

    private TransactionType typeTransaction;

    private String status;

}
