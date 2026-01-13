package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IEmployeeBusiness;
import com.asb.backCompanyService.dto.request.EmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.EmployeePaymentDTO;
import com.asb.backCompanyService.dto.responde.EmployeeResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Area;
import com.asb.backCompanyService.model.Employee;
import com.asb.backCompanyService.model.IdentificationType;
import com.asb.backCompanyService.model.Position;
import com.asb.backCompanyService.repository.AreaRepository;
import com.asb.backCompanyService.repository.EmployeeRepository;
import com.asb.backCompanyService.repository.IdentificationTypeRepository;
import com.asb.backCompanyService.repository.PositionRepository;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j

public class EmployeeService implements IEmployeeBusiness {

    private final EmployeeRepository employeeRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final PositionRepository positionRepository;
    private final AreaRepository areaRepository;
    private final EntityManager entityManager;

    @Override
    public EmployeeRequestDTO save(EmployeeRequestDTO request) {
        Employee Employee = new Employee();
        Employee.setName(request.getName());
        Employee.setPhone(request.getPhone());
        Employee.setAddress(request.getAddress());
        Employee.setStatus("ACTIVE");
        if (request.getDate() != null && !request.getDate().isEmpty()) {
            Employee.setDate(LocalDate.parse(request.getDate()));
        }
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
        if (!employeeRepository.existsById(id))
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Employee no existe");

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
        Sort sort = Sort.unsorted();

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC;

            if (orders != null && orders.equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }

            String dbField = mapDtoFieldToDbField(sortBy);
            sort = Sort.by(direction, dbField);
        }

        Pageable pageable = PageRequest.of(page, size, sort);

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
    public List<EmployeePaymentDTO> getAllEmployeePaymentNoPage(String orders, String sortBy) {
        Sort sort = Sort.unsorted();

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC;

            if (orders != null && orders.equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }

            String dbField = mapDtoFieldToDbField(sortBy);
            sort = Sort.by(direction, dbField);
        }

        List<Object[]> rawList = employeeRepository.getEmployeePaymentStatusNoPage();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return rawList.stream()
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
    }

    private String mapDtoFieldToDbField(String dtoField) {
        return switch (dtoField.toLowerCase()) {
            case "employeeid" -> "employee_id";
            case "name" -> "name_";
            case "phone" -> "phone";
            case "identification" -> "identification";
            case "hiredate" -> "hire_date";
            case "email" -> "e-mail";
            case "basesalary" -> "base_salary";
            case "areadescription" -> "description";
            case "positiondescription" -> "description";
            case "balancetopay" -> "balanceToPay";
            default -> "employee_id";
        };
    }

    @Override
    public EmployeeRequestDTO get(Long id) {
        if (!employeeRepository.existsById(id))
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Employee no existe");

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
        response.setDate(String.valueOf(new Date(String.valueOf(EmployeeOptional.get().getDate()))));

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
        if (customQuery.containsKey("id") && !customQuery.get("id").isEmpty()) {
            id = customQuery.get("id");
        }
        if (customQuery.containsKey("name") && !customQuery.get("name").isEmpty()) {
            name = customQuery.get("name");
        }
        if (customQuery.containsKey("identification") && !customQuery.get("identification").isEmpty()) {
            identification = customQuery.get("identification");
        }
        if (customQuery.containsKey("address") && !customQuery.get("address").isEmpty()) {
            address = customQuery.get("address");
        }
        if (customQuery.containsKey("baseSalary") && !customQuery.get("baseSalary").isEmpty()) {
            baseSalary = customQuery.get("baseSalary");
        }
        if (customQuery.containsKey("email") && !customQuery.get("email").isEmpty()) {
            email = customQuery.get("email");
        }
        if (customQuery.containsKey("phone") && !customQuery.get("phone").isEmpty()) {
            phone = customQuery.get("phone");
        }
        if (customQuery.containsKey("typeIdentificationName") && !customQuery.get("typeIdentificationName").isEmpty()) {
            typeIdentificationName = customQuery.get("typeIdentificationName");
        }
        if (customQuery.containsKey("areaName") && !customQuery.get("areaName").isEmpty()) {
            areaName = customQuery.get("areaName");
        }
        if (customQuery.containsKey("positionName") && !customQuery.get("positionName").isEmpty()) {
            positionName = customQuery.get("positionName");
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));


        Specification<Employee> spec = Specification.where(null);

        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), "ACTIVE"));

        if (id != null) {
            final String idParam = id;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("id").as(String.class), "%" + idParam + "%"));
        }

        if (name != null) {
            final String nameParam = name;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("name")), "%" + nameParam.toUpperCase() + "%"));
        }

        if (phone != null) {
            final String phoneParam = phone;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("phone"), "%" + phoneParam + "%"));
        }

        if (identification != null) {
            final String identificationParam = identification;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("identification"), "%" + identificationParam + "%"));
        }

        if (address != null) {
            final String addressParam = address;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("address")), "%" + addressParam.toUpperCase() + "%"));
        }

        if (email != null) {
            final String emailParam = email;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("email")), "%" + emailParam.toUpperCase() + "%"));
        }

        if (baseSalary != null) {
            final String salaryParam = baseSalary;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("baseSalary").as(String.class), "%" + salaryParam + "%"));
        }

        if (typeIdentificationName != null) {
            final String typeParam = typeIdentificationName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<IdentificationType> typeRoot = subquery.from(IdentificationType.class);
                subquery.select(typeRoot.get("id"))
                        .where(cb.like(cb.upper(typeRoot.get("name")), "%" + typeParam.toUpperCase() + "%"));
                return cb.in(root.get("identificationTypeId")).value(subquery);
            });
        }

        if (areaName != null) {
            final String areaParam = areaName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Area> areaRoot = subquery.from(Area.class);
                subquery.select(areaRoot.get("id"))
                        .where(cb.like(cb.upper(areaRoot.get("description")), "%" + areaParam.toUpperCase() + "%"));
                return cb.in(root.get("areaId")).value(subquery);
            });
        }

        if (positionName != null) {
            final String positionParam = positionName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Position> positionRoot = subquery.from(Position.class);
                subquery.select(positionRoot.get("id"))
                        .where(cb.like(cb.upper(positionRoot.get("description")), "%" + positionParam.toUpperCase() + "%"));
                return cb.in(root.get("positionId")).value(subquery);
            });
        }

        Page<Employee> entityPage = employeeRepository.findAll(spec, pagingSort);

        log.info("Resultados encontrados: {}", entityPage.getContent().size());

        return entityPage.map(this::mapToEmployeeResponseDTO);
    }

    private EmployeeResponseDTO mapToEmployeeResponseDTO(Employee entity) {
        String typeIdentificationNameValue = null;
        String areaNameValue = null;
        String positionNameValue = null;

        if (entity.getIdentificationTypeId() != null) {
            typeIdentificationNameValue = identificationTypeRepository.findById(entity.getIdentificationTypeId())
                    .map(IdentificationType::getName)
                    .orElse(null);
        }

        if (entity.getAreaId() != null) {
            areaNameValue = areaRepository.findById(entity.getAreaId())
                    .map(Area::getDescription)
                    .orElse(null);
        }

        if (entity.getPositionId() != null) {
            positionNameValue = positionRepository.findById(entity.getPositionId())
                    .map(Position::getDescription)
                    .orElse(null);
        }

        return EmployeeResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .identification(entity.getIdentification())
                .address(entity.getAddress())
                .email(entity.getEmail())
                .typeIdentificationId(entity.getIdentificationTypeId())
                .typeIdentificationName(typeIdentificationNameValue)
                .areaId(entity.getAreaId())
                .areaName(areaNameValue)
                .positionId(entity.getPositionId())
                .positionName(positionNameValue)
                .date(entity.getDate())
                .baseSalary(entity.getBaseSalary())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public Page<EmployeePaymentDTO> searchEmployeePayment(Map<String, String> customQuery) {
        int page = 0;
        int size = 10;
        String orders = "ASC";
        String sortBy = "id";

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
        if (customQuery.containsKey("sortBy") && !customQuery.get("sortBy").isEmpty()) {
            sortBy = customQuery.get("sortBy");
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
        String paymentStatusFilter = getFilterValue(customQuery, "paymentStatus");
        String totalFilter = getFilterValue(customQuery, "total");

        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(orders);
        } catch (IllegalArgumentException ignored) {
            direction = Sort.Direction.ASC;
        }

        boolean needsPostFiltering = paymentStatusFilter != null || totalFilter != null;

        Pageable fetchPageable;
        if (needsPostFiltering) {
            fetchPageable = Pageable.unpaged();
        } else {
            fetchPageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        }

        Specification<Employee> spec = Specification.where(null);

        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), "ACTIVE"));

        if (name != null) {
            final String nameParam = name;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("name")), "%" + nameParam.toUpperCase() + "%"));
        }

        if (phone != null) {
            final String phoneParam = phone;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("phone")), "%" + phoneParam.toUpperCase() + "%"));
        }

        if (identification != null) {
            final String identificationParam = identification;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("identification")), "%" + identificationParam.toUpperCase() + "%"));
        }

        if (email != null) {
            final String emailParam = email;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("email")), "%" + emailParam.toUpperCase() + "%"));
        }

        if (address != null) {
            final String addressParam = address;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("address")), "%" + addressParam.toUpperCase() + "%"));
        }

        if (baseSalary != null) {
            final String salaryParam = baseSalary;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("baseSalary").as(String.class), "%" + salaryParam + "%"));
            log.info("✅ Filtrando por salario base (LIKE): {}", salaryParam);
        }

        if (identificationTypeName != null) {
            final String typeParam = identificationTypeName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<IdentificationType> typeRoot = subquery.from(IdentificationType.class);
                subquery.select(typeRoot.get("id"))
                        .where(cb.like(cb.upper(typeRoot.get("name")), "%" + typeParam.toUpperCase() + "%"));
                return cb.in(root.get("identificationTypeId")).value(subquery);
            });
        }

        if (areaDescription != null) {
            final String areaParam = areaDescription;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Area> areaRoot = subquery.from(Area.class);
                subquery.select(areaRoot.get("id"))
                        .where(cb.like(cb.upper(areaRoot.get("description")), "%" + areaParam.toUpperCase() + "%"));
                return cb.in(root.get("areaId")).value(subquery);
            });
        }

        if (positionDescription != null) {
            final String positionParam = positionDescription;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Position> positionRoot = subquery.from(Position.class);
                subquery.select(positionRoot.get("id"))
                        .where(cb.like(cb.upper(positionRoot.get("description")), "%" + positionParam.toUpperCase() + "%"));
                return cb.in(root.get("positionId")).value(subquery);
            });
        }

        Page<Employee> entityPage = employeeRepository.findAll(spec, fetchPageable);

        log.info("Empleados encontrados: {}", entityPage.getContent().size());

        Set<Long> employeeIds = entityPage.getContent().stream()
                .map(Employee::getId)
                .collect(Collectors.toSet());

        Set<Long> identificationTypeIds = entityPage.getContent().stream()
                .map(Employee::getIdentificationTypeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> areaIds = entityPage.getContent().stream()
                .map(Employee::getAreaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> positionIds = entityPage.getContent().stream()
                .map(Employee::getPositionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, String> identificationTypeMap = identificationTypeRepository.findAllById(identificationTypeIds).stream()
                .collect(Collectors.toMap(IdentificationType::getIdentificationTypeId, IdentificationType::getName));

        Map<Long, String> areaMap = areaRepository.findAllById(areaIds).stream()
                .collect(Collectors.toMap(Area::getId, Area::getDescription));

        Map<Long, String> positionMap = positionRepository.findAllById(positionIds).stream()
                .collect(Collectors.toMap(Position::getId, Position::getDescription));

        Map<Long, Double> balanceMap = calculateBalanceForEmployees(employeeIds);

        List<EmployeePaymentDTO> dtoList = entityPage.getContent().stream()
                .map(employee -> {
                    String typeIdentificationNameVal = employee.getIdentificationTypeId() != null ?
                            identificationTypeMap.get(employee.getIdentificationTypeId()) : null;
                    String areaNameVal = employee.getAreaId() != null ?
                            areaMap.get(employee.getAreaId()) : null;
                    String positionNameVal = employee.getPositionId() != null ?
                            positionMap.get(employee.getPositionId()) : null;

                    Double baseSalaryVal = employee.getBaseSalary() != null ? employee.getBaseSalary() : 0.0;
                    Double balanceToPay = balanceMap.getOrDefault(employee.getId(), 0.0);
                    Double total = baseSalaryVal + balanceToPay;

                    String paymentStatus = total == 0.0 ? "CANCELADO" :
                            total < 0.0 ? "DEBE" : "PENDIENTE";

                    String hireDate = employee.getDate() != null ?
                            employee.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;

                    return new EmployeePaymentDTO(
                            employee.getId(),
                            employee.getName(),
                            employee.getPhone(),
                            employee.getIdentification(),
                            employee.getIdentificationTypeId(),
                            typeIdentificationNameVal,
                            employee.getAddress(),
                            hireDate,
                            employee.getEmail(),
                            employee.getAreaId(),
                            areaNameVal,
                            employee.getPositionId(),
                            positionNameVal,
                            baseSalaryVal,
                            total,
                            paymentStatus,
                            employee.getStatus()
                    );
                })
                .collect(Collectors.toList());

        if (paymentStatusFilter != null) {
            final String statusFilter = paymentStatusFilter.toUpperCase();
            log.info("🔍 Filtrando por paymentStatus: {}", statusFilter);
            dtoList = dtoList.stream()
                    .filter(dto -> dto.getPaymentStatus() != null &&
                            dto.getPaymentStatus().toUpperCase().contains(statusFilter))
                    .collect(Collectors.toList());
            log.info("✅ Después del filtro de paymentStatus: {} registros", dtoList.size());
        }

        if (totalFilter != null) {
            try {
                final double filterValue = Double.parseDouble(totalFilter);
                log.info("🔍 Filtrando por total >= {}", filterValue);
                dtoList = dtoList.stream()
                        .filter(dto -> dto.getTotal() != null && dto.getTotal() >= filterValue)
                        .collect(Collectors.toList());
                log.info("✅ Después del filtro de total: {} registros", dtoList.size());
            } catch (NumberFormatException e) {
                log.warn("Total inválido: {}", totalFilter);
            }
        }

        if (!"id".equals(sortBy.toLowerCase())) {
            sortDTOList(dtoList, sortBy, direction);
        }

        if (needsPostFiltering) {
            int totalElements = dtoList.size();
            int start = page * size;
            int end = Math.min(start + size, dtoList.size());

            if (start >= dtoList.size()) {
                dtoList = Collections.emptyList();
            } else {
                dtoList = dtoList.subList(start, end);
            }

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return new PageImpl<>(dtoList, pagingSort, totalElements);
        }

        return new PageImpl<>(dtoList, PageRequest.of(page, size, Sort.by(direction, sortBy)), entityPage.getTotalElements());
    }

    private Map<Long, Double> calculateBalanceForEmployees(Set<Long> employeeIds) {
        if (employeeIds.isEmpty()) {
            return new HashMap<>();
        }

        try {
            List<Object[]> results = employeeRepository.calculateBalancesByEmployeeIds(employeeIds);

            return results.stream()
                    .collect(Collectors.toMap(
                            row -> ((Number) row[0]).longValue(),
                            row -> row[1] != null ? ((Number) row[1]).doubleValue() : 0.0
                    ));
        } catch (Exception e) {
            log.error("Error calculando balances: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    private void sortDTOList(List<EmployeePaymentDTO> dtoList, String sortBy, Sort.Direction direction) {
        Comparator<EmployeePaymentDTO> comparator = Comparator.comparing(
                dto -> {
                    Object key = switch (sortBy.toLowerCase()) {
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

        if (direction.isDescending()) {
            comparator = comparator.reversed();
        }

        dtoList.sort(comparator);
    }

    private String getFilterValue(Map<String, String> customQuery, String key) {
        if (customQuery.containsKey(key) && !customQuery.get(key).trim().isEmpty()) {
            return customQuery.get(key).trim();
        }
        return null;
    }


}
