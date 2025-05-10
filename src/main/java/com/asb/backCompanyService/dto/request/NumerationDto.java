package com.asb.backCompanyService.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumerationDto {

    private Long id;
    private Long accountingDocumentTypeId;
    private String authNumer;
    private String prefix;
    private LocalDate startDate = LocalDate.now();
    private LocalDate finishDate = LocalDate.now()  ;
    private String status;
    private Integer initialNumber;
    private Integer finalNumber;
    private Integer currentNumber;
    private String technicalKey;
}
