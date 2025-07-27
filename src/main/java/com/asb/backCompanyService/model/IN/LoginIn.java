/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asb.backCompanyService.model.IN;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.asb.backCompanyService.util.LoginMode;

/**
 *
 * @author manuelm
 */
@Data
public class LoginIn {
    @NotBlank(message = "The username is required.")
    private String username;

    @NotBlank(message = "The password is required.")
    private String password;

    @NotBlank(message = "The loginMode is required.")
    private LoginMode loginMode;

    public LoginIn() {
    }
}
