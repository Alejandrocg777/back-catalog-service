package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateNeighborhoodDtoResponse {
    private Long id;
    private Long cityId;
    private String cityName;
    private Long departmentId;
    private String departmentName;
    private String neighborhood;
    private Double rate;
    private String status;
}
