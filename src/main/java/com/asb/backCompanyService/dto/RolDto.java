package com.asb.backCompanyService.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class RolDto {

    private long id;
    private String name;

    public RolDto() {
    }

}
