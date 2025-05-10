package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IDepartmentBusiness;
import com.asb.backCompanyService.dto.request.DepartmentDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${app.request.prefix}/${app.request.version}${app.request.mappings}/department")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class DepartmentController {

    private final IDepartmentBusiness iDepartmentBusiness;

    @PostMapping("/create")
    public ResponseEntity<DepartmentDto> save(@RequestBody DepartmentDto departmentDto) {
        DepartmentDto savedDepartment = iDepartmentBusiness.save(departmentDto);
        return ResponseEntity.ok(savedDepartment);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DepartmentDto> get(@PathVariable("id") long id) {
        DepartmentDto departmentDto = iDepartmentBusiness.get(id);
        return ResponseEntity.ok(departmentDto);
    }

    @GetMapping
    public ResponseEntity<Page<Department>> getAll(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "ASC") String orders,
                                                   @RequestParam(defaultValue = "department_id") String sortBy) {
        Page<Department> departments = iDepartmentBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Department>> search(@RequestParam Map<String, String> customQuery) {
        Page<Department> departments = iDepartmentBusiness.searchDeparment(customQuery);
        return ResponseEntity.ok(departments);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long departmentId,
                                                     @RequestBody DepartmentDto departmentDto) {
        log.info("Iniciando actualización para Department con ID: {} y DTO: {}", departmentId, departmentDto);
        GenericResponse response = iDepartmentBusiness.update(departmentId, departmentDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        iDepartmentBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllDepartments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        log.info("Starting endpoint to get all departments");
        List<Department> departments = iDepartmentBusiness.getAllDeparment();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }
}
