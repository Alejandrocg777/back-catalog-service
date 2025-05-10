package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.DepartmentDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Department;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IDepartmentBusiness {
    DepartmentDto save(DepartmentDto departmentDto);


    GenericResponse update(Long id, DepartmentDto departmentDto);


    boolean delete(Long id);


    DepartmentDto get(Long id);


    Page<Department> getAll(int page, int size, String orders, String sortBy);

    Page<Department> searchDeparment(Map<String, String> customQuery);

    List<Department> getAllDeparment();
}
