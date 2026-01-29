package com.asb.backCompanyService.dto.responde;

import com.asb.backCompanyService.dto.request.OrderAllocationDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAllocationResponseDto {

    private Long id;
    private Long transporterId;
    private String transporterName;
    private Long pendingOrderId;
    private Long originWarehouseId;
    private String warehouseName;
    private Long destinationNeighborhoodId;
    private String neighborhoodName;
    private LocalDate date;
    private LocalTime hour;
    private Boolean chargeInvoice;
    private String address;
    private String phone;
    private String observations;
    private Double total;
    private String status;
    private String image;
    private String signature;
    private String statusOrderAllocation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long customerId;
    private String customerName;
    private Long paymentMethodId;
    private String paymentMethodName;


    private List<OrderAllocationDetailDto> products;


    public OrderAllocationResponseDto(Long id, Long transporterId, String transporterName,
                                      Long pendingOrderId, Long originWarehouseId, String warehouseName,
                                      Long destinationNeighborhoodId, String neighborhoodName,
                                      LocalDate date,  LocalTime hour, Boolean chargeInvoice,
                                      String address, String phone, String observations,
                                      Double total, String status, String image,String signature,
                                      String statusOrderAllocation,
                                      LocalDateTime createdAt, LocalDateTime updatedAt,
                                      Long customerId, String customerName,
                                      Long paymentMethodId, String paymentMethodName) {
        this.id = id;
        this.transporterId = transporterId;
        this.transporterName = transporterName;
        this.pendingOrderId = pendingOrderId;
        this.originWarehouseId = originWarehouseId;
        this.warehouseName = warehouseName;
        this.destinationNeighborhoodId = destinationNeighborhoodId;
        this.neighborhoodName = neighborhoodName;
        this.date = date;
        this.hour = hour;
        this.chargeInvoice = chargeInvoice;
        this.address = address;
        this.phone = phone;
        this.observations = observations;
        this.total = total;
        this.status = status;
        this.image = image;
        this.signature = signature;
        this.statusOrderAllocation = statusOrderAllocation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customerId = customerId;
        this.customerName = customerName;
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
    }
}