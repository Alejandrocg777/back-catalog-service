package com.asb.backCompanyService.dto.responde;

import jakarta.persistence.Column;
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

    private String address;

    private String neighborhood;

    private String email;

    private String cityName;

    private String status;
}
