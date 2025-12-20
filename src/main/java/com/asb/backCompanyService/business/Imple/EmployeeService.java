package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IEmployeeBusiness;
import com.asb.backCompanyService.dto.request.EmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.EmployeeResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Employee;
import com.asb.backCompanyService.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j

public class EmployeeService implements IEmployeeBusiness{

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeRequestDTO save(EmployeeRequestDTO request) {
        Employee Employee = new Employee();
        Employee.setName(request.getName());
        Employee.setPhone(request.getPhone());
        Employee.setAddress(request.getAddress());
        Employee.setStatus("ACTIVE");
        Employee.setDate(request.getDate());
        Employee.setEmail(request.getEmail());
        Employee.setIdentification(request.getIdentification());
        Employee.setIdentificationTypeId(request.getTypeIdentificationId());
        Employee.setAreaId(request.getAreaId());
        Employee.setPositionId(request.getPositionId());
        Employee.setBaseSalary(request.getBaseSalary());
        Employee newEmployee = employeeRepository.save(Employee);

        EmployeeRequestDTO response = new EmployeeRequestDTO();
        BeanUtils.copyProperties(newEmployee, response);
        return response;
    }

    @Override
    public GenericResponse update(Long id, EmployeeRequestDTO requestDTO) {
        if (!employeeRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Employee no existe");

        Optional<Employee> EmployeeOptional = employeeRepository.findById(id);

        Employee Employee = EmployeeOptional.get();
        Employee.setName(requestDTO.getName());
        Employee.setStatus(requestDTO.getStatus());
        Employee.setIdentification(requestDTO.getIdentification());
        Employee.setAddress(requestDTO.getAddress());
        Employee.setEmail(requestDTO.getEmail());
        Employee.setPhone(requestDTO.getPhone());
        Employee.setIdentificationTypeId(requestDTO.getTypeIdentificationId());
        Employee.setAreaId(requestDTO.getAreaId());
        Employee.setPositionId(requestDTO.getPositionId());
        Employee.setBaseSalary(requestDTO.getBaseSalary());
        employeeRepository.save(Employee);

        return new GenericResponse("Employee actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (employeeRepository.existsById(id)) {
            Employee Employee = employeeRepository.findById(id).get();
            Employee.setStatus("INACTIVE");
            employeeRepository.save(Employee);
            return true;
        } else {
            throw new RuntimeException("el Employeee no fue encontrada por el id " + id);
        }
    }

    @Override
    public Page<EmployeeResponseDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return employeeRepository.getStatus(pagingSort);
    }

    @Override
    public EmployeeRequestDTO get(Long id) {
        if (!employeeRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Employee no existe");

        Optional<Employee> EmployeeOptional = employeeRepository.findById(id);

        EmployeeRequestDTO response = new EmployeeRequestDTO();
        response.setName(EmployeeOptional.get().getName());
        response.setAddress(EmployeeOptional.get().getAddress());
        response.setStatus(EmployeeOptional.get().getStatus());
        response.setPhone(EmployeeOptional.get().getPhone());
        response.setIdentification(EmployeeOptional.get().getIdentification());
        response.setAreaId(EmployeeOptional.get().getAreaId());
        response.setPositionId(EmployeeOptional.get().getPositionId());
        response.setBaseSalary(EmployeeOptional.get().getBaseSalary());
        response.setDate(String.valueOf(new Date(EmployeeOptional.get().getDate())));

        return response;
    }

    @Override
    public List<Employee> getAllNoPage() {
        try {
            return employeeRepository.getAllNoPage();
        } catch (Exception e) {
            log.error("Error al obtener el inventario");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el inventario", e);
        }
    }

    @Override
    public Page<EmployeeResponseDTO> search(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String name = null;
        String phone = null;
        String identification = null;
        String address = null;
        String status = null;
        String email = null;
        String baseSalary = null;
        String typeIdentificationName = null;
        String areaName = null;
        String positionName = null;


        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }

        if (customQuery.containsKey("name")) {
            name = "%" + customQuery.get("name") + "%";
        }

        if (customQuery.containsKey("identification")) {
            identification = "%" + customQuery.get("identification") + "%";
        }

        if (customQuery.containsKey("address")) {
            address = "%" + customQuery.get("address") + "%";
        }

        if (customQuery.containsKey("baseSalary")) {
            baseSalary = "%" + customQuery.get("baseSalary") + "%";
        }

        if (customQuery.containsKey("email")) {
            email = "%" + customQuery.get("email") + "%";
        }

        if (customQuery.containsKey("phone")) {
            phone = "%" + customQuery.get("phone") + "%";
        }

        if (customQuery.containsKey("typeIdentificationName")) {
            typeIdentificationName = "%" + customQuery.get("typeIdentificationName") + "%";
        }

        if (customQuery.containsKey("areaName")) {
            areaName = "%" + customQuery.get("areaName") + "%";
        }

        if (customQuery.containsKey("positionName")) {
            positionName = "%" + customQuery.get("positionName") + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<EmployeeResponseDTO> searchEmployee = employeeRepository.search(id, name, phone, identification, address,  status,  email,baseSalary, typeIdentificationName,areaName,positionName, pagingSort);

        return searchEmployee;
    }
}
