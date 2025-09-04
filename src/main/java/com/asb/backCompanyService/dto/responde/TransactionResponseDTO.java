package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private String productName;
    private String transactionType;
    private Long quantity;
    private LocalDateTime date;
    private String typeUser;
    private String userName;
    private Double value;
    private String status;

}
