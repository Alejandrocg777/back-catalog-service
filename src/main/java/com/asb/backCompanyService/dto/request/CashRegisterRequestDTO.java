package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CashRegisterRequestDTO {

    private Long id;
    private String cashRegisterCode;
    private String description;
    private String status;

}
