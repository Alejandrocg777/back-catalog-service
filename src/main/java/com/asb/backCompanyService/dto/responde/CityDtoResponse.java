package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDtoResponse {
    private Long id;
    private String cityCode;
    private String cityName;
    private String departmentName;
    private Long departmentId;
    private String status;
}
