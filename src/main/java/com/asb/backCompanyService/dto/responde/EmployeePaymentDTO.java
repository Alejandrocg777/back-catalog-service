package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmployeePaymentDTO {


    private Long id;

    private String name;

    private String phone;

    private String identification;

    private Long typeIdentificationId;

    private String typeIdentificationName;

    private String address;

    private String date;

    private String email;

    private Long areaId;

    private String areaName;

    private Long positionId;

    private String positionName;

    private Double baseSalary;

    private Double total;

    private String paymentStatus;
    
    private String status;


}
