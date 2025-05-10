package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MethodPaymentRequestDTO{

    private Long id;

    private String type;

    private Long code;

    private String status;

}
