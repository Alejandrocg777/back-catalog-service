package com.asb.backCompanyService.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class ForgotPasswordUserDto {

    private String password;
    private String email;

    public ForgotPasswordUserDto() {
    }

}