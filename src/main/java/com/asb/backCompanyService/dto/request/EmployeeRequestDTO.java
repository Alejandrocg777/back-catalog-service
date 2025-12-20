package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeRequestDTO {

    private Long id;

    private String name;

    private String phone;

    private String identification;

    private Long typeIdentificationId;

    private String address;

    private String date;

    private String email;

    private Long areaId;

    private Long positionId;

    private Double baseSalary;

    private String status;
}
