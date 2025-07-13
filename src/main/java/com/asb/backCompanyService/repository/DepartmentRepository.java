package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = "SELECT * FROM department " +
            "WHERE CAST(department_id AS char) LIKE :id " +
            "OR UPPER(department_code) LIKE UPPER(:departmentCode) " +
            "OR UPPER(department_name) LIKE UPPER(:departmentName) " +
            "OR UPPER(status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) FROM department " +
                    "WHERE CAST(department_id AS char) LIKE :id " +
                    "OR UPPER(department_code) LIKE UPPER(:departmentCode) " +
                    "OR UPPER(department_name) LIKE UPPER(:departmentName) " +
                    "OR UPPER(status) LIKE UPPER(:status)",
            nativeQuery = true)
    Page<Department> searchDepartment(String id, String departmentCode, String departmentName, String status, Pageable pageable);


    @Query(value = "SELECT * FROM department " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<Department> getStatus(Pageable pageable);

    boolean existsById(Long id);
}
