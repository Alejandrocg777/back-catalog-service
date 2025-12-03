package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientRequestDTO {

    private Long id;

    private String name;

    private String phone;

    private String identification;

    private String address;

    private String neighborhoodName;

    private String email;

    private Long cityId;

    private String status;

    private Long typeIdentificationId;

    private Long verificationDigit;

    private Long personTypeId;

    private Long taxLiabilityId;

    private Long departmentId;
}
