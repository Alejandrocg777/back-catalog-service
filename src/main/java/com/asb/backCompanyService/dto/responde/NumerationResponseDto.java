package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumerationResponseDto {
    private Long id;
    private Long accountingDocumentTypeId;
    private String authNumer;
    private String prefix;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String status;
    private Integer initialNumber;
    private Integer finalNumber;
    private Integer currentNumber;
    private String descriptionAccountingDocumentType;
    private String technicalKey;
}
