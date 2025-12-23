package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IEmployeeBusiness;
import com.asb.backCompanyService.dto.request.EmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.EmployeePaymentDTO;
import com.asb.backCompanyService.dto.responde.EmployeeResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Employee;
import com.asb.backCompanyService.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public Page<EmployeePaymentDTO> getAllEmployeePayment(int page, int size, String orders, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        Page<Object[]> rawPage = employeeRepository.getEmployeePaymentStatus(pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<EmployeePaymentDTO> dtoList = rawPage.getContent().stream()
                .map(row -> {
                    String hireDateStr = null;
                    if (row[7] != null) {
                        java.sql.Date sqlDate = (java.sql.Date) row[7];
                        hireDateStr = sqlDate.toLocalDate().format(formatter);
                    }
                    Double baseSalary = null;
                    if (row[13] != null) {
                        baseSalary = ((BigDecimal) row[13]).doubleValue();
                    }

                    Double transactionBalance = 0.0;
                    if (row[15] != null) {
                        transactionBalance = ((BigDecimal) row[15]).doubleValue();
                    }
                    Double totalToPay = transactionBalance;
                    if (baseSalary != null) {
                        totalToPay += baseSalary;
                    }

                    String paymentStatus;
                    if (totalToPay == 0) {
                        paymentStatus = "CANCELADO";
                    } else if (totalToPay < 0) {
                        paymentStatus = "DEBE";
                    } else {
                        paymentStatus = "PENDIENTE";
                    }

                    return new EmployeePaymentDTO(
                            (Long) row[0],
                            (String) row[1],
                            (String) row[2],
                            (String) row[3],
                            (Long) row[4],
                            (String) row[5],
                            (String) row[6],
                            hireDateStr,
                            (String) row[8],
                            (Long) row[9],
                            (String) row[10],
                            (Long) row[11],
                            (String) row[12],
                            baseSalary,
                            totalToPay,
                            paymentStatus,
                            (String) row[14]
                    );
                })
                .toList();

        return new PageImpl<>(dtoList, pageable, rawPage.getTotalElements());
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

    @Override
    public Page<EmployeePaymentDTO> searchEmployeePayment(Map<String, String> customQuery) {
        int page = 0;
        int size = 10;
        String orders = "ASC";
        final String sortByParam = customQuery.getOrDefault("sortBy", "id").toLowerCase();
        if (customQuery.containsKey("page")) {
            try {
                page = Integer.parseInt(customQuery.get("page"));
                if (page < 0) page = 0;
            } catch (NumberFormatException ignored) {
                page = 0;
            }
        }
        if (customQuery.containsKey("size")) {
            try {
                size = Integer.parseInt(customQuery.get("size"));
                if (size <= 0) size = 10;
            } catch (NumberFormatException ignored) {
                size = 10;
            }
        }
        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders").toUpperCase();
        }

        String paymentStatusFilter;
        if (customQuery.containsKey("paymentStatus") && !customQuery.get("paymentStatus").trim().isEmpty()) {
            paymentStatusFilter = customQuery.get("paymentStatus").trim().toUpperCase();
        } else {
            paymentStatusFilter = null;
        }

        String totalFilter;
        if (customQuery.containsKey("total") && !customQuery.get("total").trim().isEmpty()) {
            totalFilter = customQuery.get("total").trim();
        } else {
            totalFilter = null;
        }

        String name = getFilterValue(customQuery, "name");
        String phone = getFilterValue(customQuery, "phone");
        String identification = getFilterValue(customQuery, "identification");
        String email = getFilterValue(customQuery, "email");
        String address = getFilterValue(customQuery, "address");
        String identificationTypeName = getFilterValue(customQuery, "typeIdentificationName");
        String areaDescription = getFilterValue(customQuery, "areaName");
        String positionDescription = getFilterValue(customQuery, "positionName");
        String baseSalary = getFilterValue(customQuery, "baseSalary");

        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(orders);
        } catch (IllegalArgumentException ignored) {
            direction = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "employeeId"));

        Page<Object[]> rawPage = employeeRepository.searchEmployeePayment(
                name, phone, identification, email, address,
                identificationTypeName, areaDescription, positionDescription,
                baseSalary, pageable);

        List<EmployeePaymentDTO> dtoList = rawPage.getContent().stream()
                .map(row -> {
                    Long id = (Long) row[0];
                    String empName = (String) row[1];
                    String empPhone = (String) row[2];
                    String empIdentification = (String) row[3];
                    Long typeIdentificationId = (Long) row[4];
                    String typeIdentificationNameVal = (String) row[5];
                    String empAddress = (String) row[6];
                    String hireDate = (String) row[7];
                    String empEmail = (String) row[8];
                    Long areaId = (Long) row[9];
                    String areaName = (String) row[10];
                    Long positionId = (Long) row[11];
                    String positionName = (String) row[12];
                    Double baseSalaryVal = row[13] != null ? ((BigDecimal) row[13]).doubleValue() : 0.0;
                    String status = (String) row[14];
                    Double balanceToPay = row[15] != null ? ((BigDecimal) row[15]).doubleValue() : 0.0;

                    Double total = baseSalaryVal + balanceToPay;

                    String paymentStatus = total == 0.0 ? "CANCELADO" :
                            total < 0.0 ? "DEBE" : "PENDIENTE";

                    return new EmployeePaymentDTO(
                            id, empName, empPhone, empIdentification,
                            typeIdentificationId, typeIdentificationNameVal,
                            empAddress, hireDate, empEmail,
                            areaId, areaName, positionId, positionName,
                            baseSalaryVal, total, paymentStatus, status
                    );
                })
                .collect(Collectors.toCollection(ArrayList::new));

        if (paymentStatusFilter != null) {
            dtoList = dtoList.stream()
                    .filter(dto -> dto.getPaymentStatus() != null &&
                            dto.getPaymentStatus().toUpperCase().contains(paymentStatusFilter))
                    .collect(Collectors.toList());
        }

        if (totalFilter != null) {
            try {
                double filterValue = Double.parseDouble(totalFilter);
                dtoList = dtoList.stream()
                        .filter(dto -> dto.getTotal() != null && dto.getTotal().toString().contains(totalFilter))
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {

            }
        }

        final Sort.Direction finalDirection = direction;

        if (!"id".equals(sortByParam) && !"employeeid".equals(sortByParam)) {
            Comparator<EmployeePaymentDTO> comparator = Comparator.comparing(
                    dto -> {
                        Object key = switch (sortByParam) {
                            case "paymentstatus" -> dto.getPaymentStatus();
                            case "total" -> dto.getTotal();
                            case "name" -> dto.getName();
                            case "phone" -> dto.getPhone();
                            case "identification" -> dto.getIdentification();
                            case "email" -> dto.getEmail();
                            case "basesalary" -> dto.getBaseSalary();
                            default -> dto.getId();
                        };
                        return key != null ? key.toString() : "";
                    }
            );

            if (finalDirection.isDescending()) {
                comparator = comparator.reversed();
            }

            dtoList.sort(comparator);
        }

        long totalElements = dtoList.size();

        return new PageImpl<>(dtoList, pageable, totalElements);
    }


    private String getFilterValue(Map<String, String> customQuery, String key) {
        String value = customQuery.get(key);
        if (value != null && !value.trim().isEmpty()) {
            return "%" + value.trim() + "%";
        }
        return null;
    }
}
