package com.asb.backCompanyService.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailDto {
    private Long id;
    private String itemCode;
    private String description;
    private String destination;
    private String itemType;
    private Integer requestedQuantity;
    private String observations;
    private Long requestId;
    private String status;
}
