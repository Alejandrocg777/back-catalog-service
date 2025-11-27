package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountOwesSupplierDTO {

    private Long id;
    private String productName;
    private Long remainingAmount;
}
