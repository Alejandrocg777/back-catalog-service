package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IEmployeeBusiness;
import com.asb.backCompanyService.dto.request.EmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.EmployeePaymentDTO;
import com.asb.backCompanyService.dto.responde.EmployeeResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Employee;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/employee")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class EmployeeController {

    private final IEmployeeBusiness employeeBusiness;

    @PostMapping("/create")
    public ResponseEntity<EmployeeRequestDTO> save(@RequestBody EmployeeRequestDTO requestDTO) {
        EmployeeRequestDTO savedEmployee = employeeBusiness.save(requestDTO);
        return ResponseEntity.ok(savedEmployee);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody EmployeeRequestDTO requestDTO) {
        log.info("Iniciando actualización para Employee con ID: {} y DTO: {}", id, requestDTO);
        GenericResponse response = employeeBusiness.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        employeeBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        Page<EmployeeResponseDTO> Employees = employeeBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(Employees);
    }

    @GetMapping("/get-all-payment")
    public ResponseEntity<Page<EmployeePaymentDTO>> getAllEmployeePayment(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "5") int size,
                                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                                          @RequestParam(defaultValue = "id") String sortBy) {

        Page<EmployeePaymentDTO> result = employeeBusiness.getAllEmployeePayment(page, size, orders, sortBy);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<EmployeeRequestDTO> get(@PathVariable("id") long id) {
        EmployeeRequestDTO requestDTO = employeeBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    @GetMapping("/search-payment")
    public ResponseEntity<Page<EmployeePaymentDTO>> searchEmployeePayment(@RequestParam Map<String, String> customQuery) {
        Page<EmployeePaymentDTO> employeeSearch = employeeBusiness.searchEmployeePayment(customQuery);
        return ResponseEntity.ok(employeeSearch);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponseDTO>> search(@RequestParam Map<String, String> customQuery) {
        Page<EmployeeResponseDTO> Employee = employeeBusiness.search(customQuery);
        return ResponseEntity.ok(Employee);
    }

    @GetMapping("/no-page/getAllEmployee")
    public ResponseEntity<List<Employee>> getAllNoPage() {
        log.info("Iniciando endpoint para obtener todas las empleados");
        List<Employee> response = employeeBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
