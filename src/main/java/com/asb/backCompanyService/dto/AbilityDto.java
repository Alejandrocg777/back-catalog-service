package com.asb.backCompanyService.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class AbilityDto {

    private String action;
    private String subject;

    public AbilityDto() {
    }

}