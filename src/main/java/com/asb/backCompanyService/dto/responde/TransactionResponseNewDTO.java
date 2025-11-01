package com.asb.backCompanyService.dto.responde;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseNewDTO {

    private Long id;
    private Long rolId;
    private String typeUser;
    private Long userId;
    private String userName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime date;
    private String transactionType;
    private String observation;
}
