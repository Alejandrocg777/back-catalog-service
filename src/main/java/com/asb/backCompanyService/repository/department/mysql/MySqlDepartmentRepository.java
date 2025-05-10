package com.asb.backCompanyService.repository.department.mysql;

import com.asb.backCompanyService.model.Department;
import com.asb.backCompanyService.repository.department.BaseRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Profile("mysql")
@Repository
public interface MySqlDepartmentRepository extends BaseRepository<Department, Long> {

    @Override
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

    @Override
    @Query(value = "SELECT * FROM department " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<Department> getStatus(Pageable pageable);

    boolean existsById(Long id);
}
