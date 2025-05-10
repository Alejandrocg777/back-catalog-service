/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asb.backCompanyService.dto.responde;

/**
 *
 * @author manuelm
 */
public class GenericResponse {

    private String message;
    private int statusCode;

    public GenericResponse() {
    }

    public GenericResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public GenericResponse(String success, String s) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    

}
