package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.EmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.EmployeePaymentDTO;
import com.asb.backCompanyService.dto.responde.EmployeeResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IEmployeeBusiness {

    EmployeeRequestDTO save(EmployeeRequestDTO requestDTO);
    GenericResponse update(Long id, EmployeeRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<EmployeeResponseDTO> getAll(int page, int size, String orders, String sortBy);

    Page<EmployeePaymentDTO> getAllEmployeePayment(int page, int size, String orders, String sortBy);

    List<EmployeePaymentDTO> getAllEmployeePaymentNoPage(String orders, String sortBy);

    EmployeeRequestDTO get(Long id);
    List<Employee> getAllNoPage();
    Page<EmployeeResponseDTO>search(Map<String , String>customQuery);
    Page<EmployeePaymentDTO>searchEmployeePayment(Map<String , String>customQuery);
}
