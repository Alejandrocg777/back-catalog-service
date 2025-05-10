package com.asb.backCompanyService.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private String requesterName;
    private LocalDate requestDate;
    private String area;
    private String department;
    private String requestNumber;
    private String status;
}
