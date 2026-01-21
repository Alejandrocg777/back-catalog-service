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

    private Long id;
    private Long billId;
    private Long customerId;
    private String customerName;
    private String cityName;
    private String neighborhood;
    private String address;
    private String phone;
    private String observations;
    private String date;
    private Double total;
    private String statusOrder;
}