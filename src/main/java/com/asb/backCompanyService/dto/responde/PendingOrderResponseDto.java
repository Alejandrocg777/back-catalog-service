package com.asb.backCompanyService.dto.responde;

import com.asb.backCompanyService.dto.request.PendingOrderDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingOrderResponseDto {

    private Long billId;
    private Long customerId;
    private String customerName;
    private String neighborhood;
    private String address;
    private String phone;
    private String observations;
    private LocalDateTime date;
    private Double total;
}