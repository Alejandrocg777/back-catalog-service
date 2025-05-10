package com.asb.backCompanyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {

    private Long storeId;

    private String storeName;

    private BigDecimal capacity;

    private String serviceType;

    private Long storeTypeId;
}
