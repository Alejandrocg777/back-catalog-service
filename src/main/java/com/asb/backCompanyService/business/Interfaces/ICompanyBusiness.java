package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.CompanyDto;
import com.asb.backCompanyService.dto.request.DepartmentDto;
import com.asb.backCompanyService.dto.responde.CompanyResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Company;
import com.asb.backCompanyService.model.Department;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ICompanyBusiness {

    CompanyDto save(CompanyDto companyDto);
    CompanyDto update(Long id, CompanyDto companyDto);
    boolean delete(Long id);
    CompanyResponseDto get(Long id);
    List<Company> getAllCompany();

    boolean setStatus(Long id, String status);

    ///////////////////////////////////

    Page<CompanyResponseDto> getAll(int page, int size, String orders, String sortBy);

    Page<Company> searchCompany(Map<String, String> customQuery);

}
