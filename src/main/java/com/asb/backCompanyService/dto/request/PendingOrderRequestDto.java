package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingOrderRequestDto {

    private Long id;
    private Long billId;
    private Long customerId;
    private Long paymentMethodId;
    private String customerName;
    private String neighborhood;
    private String cityName;
    private String address;
    private String phone;
    private String observations;
    private String date;
    private Double total;
    private String statusOrder;
    private List<PendingOrderDetailDto> pendingOrderDetails;
}
