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

    private String address;

    private String cityName;

    private String status;
}
