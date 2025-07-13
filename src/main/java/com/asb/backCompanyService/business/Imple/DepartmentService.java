package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IDepartmentBusiness;
import com.asb.backCompanyService.dto.request.DepartmentDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Department;
import com.asb.backCompanyService.repository.DepartmentRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class DepartmentService implements IDepartmentBusiness {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public DepartmentDto save(DepartmentDto departmentDto) {

        try {
            System.out.println("departmentDto.toString() " + departmentDto.toString());
            Boolean objectExists = false;
            if (departmentDto.getId() != null) {
                objectExists = departmentRepository.existsById(departmentDto.getId());
            }
            DepartmentDto objectDtoVo = new DepartmentDto();
            if (!objectExists) {
                Department departmentRepo = new Department();
                departmentRepo.setDepartmentCode(departmentDto.getDepartmentCode());
                departmentRepo.setDepartmentName(departmentDto.getDepartmentName());
                departmentRepo.setStatus("ACTIVE");

                Department newObject = departmentRepository.save(departmentRepo);

                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;
            } else {
                if (objectExists) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El departamento ya existe");
                } else {
                    throw new RuntimeException("Error");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el departamento", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, DepartmentDto departmentDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando método de actualización para Department con ID: {} y DepartmentDto: {}", id, departmentDto);

            Optional<Department> optionalEconomicActivity = departmentRepository.findById(id);
            if (!optionalEconomicActivity.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El departamento no existe");
            }

            Department department = optionalEconomicActivity.get();
            BeanUtils.copyProperties(departmentDto, department);
            department.setDepartmentCode(departmentDto.getDepartmentCode());
            department.setDepartmentName(departmentDto.getDepartmentName());
            departmentRepository.save(department);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Departamento actualizado");
        } catch (Exception e) {
            log.error("Error al actualizar el departamento: {}", e.getMessage());
            throw new RuntimeException("Error", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (departmentRepository.existsById(id)) {
            Department department = departmentRepository.findById(id).get();
            department.setStatus("INACTIVE");
            departmentRepository.save(department);
            return true;
        } else {
            throw new RuntimeException("El departamento no fue encontrado por el id " + id);
        }
    }

    @Override
    public DepartmentDto get(Long id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        DepartmentDto departmentDto = null;
        if (departmentOptional.isPresent()) {
            departmentDto = new DepartmentDto();
            BeanUtils.copyProperties(departmentOptional.get(), departmentDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El departamento no existe");
        }
        return departmentDto;
    }

    @Override
    public Page<Department> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return departmentRepository.getStatus(pagingSort);
    }



    @Override
    public Page<Department> searchDeparment(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "department_id";
        int page = 0;
        int size = 6;
        String id = null;
        String departmentCode = null;
        String departmentName = null;
        String status = null;

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }
        if (customQuery.containsKey("departmentCode")) {
            departmentCode = "%" + customQuery.get("departmentCode") + "%";
        }
        if (customQuery.containsKey("departmentName")) {
            departmentName = "%" + customQuery.get("departmentName").toUpperCase() + "%";
        }
        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("ID: " + id);
        log.info("Dept Code: " + departmentCode);
        log.info("Dept Name: " + departmentName);
        log.info("Status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<Department> searchResult = departmentRepository.searchDepartment(id, departmentCode, departmentName, status, pagingSort);
       log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<Department> getAllDeparment() {
        try {
            return departmentRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el departamento");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el departamento", e);
        }
    }
}
