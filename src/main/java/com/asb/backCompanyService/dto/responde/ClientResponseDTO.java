package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientResponseDTO {


    private Long id;

    private String name;

    private String phone;

    private String identification;

    private String identificationType;

    private Long typeIdentificationId;

    private String address;

    private String neighborhoodName;

    private String email;

    private String municipality;

    private Long municipalityId;

    private Long verificationDigit;

    private String personType;

    private Long personTypeId;

    private Long taxLiabilityId;

    private String taxLiability;

    private String status;
}
