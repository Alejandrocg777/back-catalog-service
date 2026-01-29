package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAllocationDto {

    // Coincide con AssignOrderTypes del frontend
    private Long id;
    private Long userId;
    private String userName;
    private Long warehouseId;
    private String warehouseName;
    private Long neighborhoodRateId;
    private String neighborhoodName;
    private String address;
    private Long orderId;
    private String customerName;
    private String customerAddress;
    private String customerCity;
    private String customerPhone;
    private Double totalPurchase;
    private LocalDate date;
    private LocalTime hour;
    private String observation;
    private String statusOrder;
    private String status;
    private Boolean chargeInvoice;

    // Lista de productos
    private List<OrderAllocationDetailDto> products;
}