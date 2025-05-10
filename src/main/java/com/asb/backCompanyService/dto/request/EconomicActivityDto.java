package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EconomicActivityDto {
        private Long id;
        private String ciiuCode;
        private String description;
        private String status;

}
