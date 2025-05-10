package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SellerRequestDTO {

    private Long id;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private String status;

}
