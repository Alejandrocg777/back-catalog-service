package com.asb.backCompanyService.dto;


import com.asb.backCompanyService.model.Company;
import com.asb.backCompanyService.model.EntityArea;
import com.asb.backCompanyService.model.EntityCompany;
import com.asb.backCompanyService.model.EntityPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GgpUserGetAllDto {

    private long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private RolDto rol;
    private long rolId;
    private EntityPosition position;
    private EntityCompany company;
    private EntityArea Area;
    private List<AbilityDto> ability;

    private Date tokenDateExpired;
    private String token;
    private String status;

    public GgpUserGetAllDto() {
    }

}