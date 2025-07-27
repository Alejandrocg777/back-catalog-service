/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asb.backCompanyService.business.Interfaces;


import com.asb.backCompanyService.model.IN.LoginIn;
import com.asb.backCompanyService.model.OUT.LoginOut;

/**
 *
 * @author manuelm
 */
public interface ILoginService {

    public LoginOut login(LoginIn loginIn);
}
