package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeResponseDTO {


    private Long id;

    private String name;

    private String phone;

    private String identification;

    private Long typeIdentificationId;

    private String typeIdentificationName;

    private String address;

    private LocalDate date;

    private String email;

    private Long areaId;

    private String areaName;

    private Long positionId;

    private String positionName;

    private Double baseSalary;
    
    private String status;


}
