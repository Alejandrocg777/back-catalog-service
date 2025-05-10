package com.asb.backCompanyService.repository.department.postgresql;

import com.asb.backCompanyService.model.Department;
import com.asb.backCompanyService.repository.department.BaseRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Profile("postgres")
@Repository
public interface PosgreSqlDepartmentRepository extends BaseRepository<Department, Long> {

    @Override
    @Query(value = "SELECT * FROM department " +
            "WHERE TO_CHAR(department_id,'999999999' ) LIKE :id " +
            "OR TO_CHAR(department_code,'999999999' ) LIKE upper(:departmentCode) " +
            "OR upper(department_name) LIKE upper(:departmentName) " +
            "OR upper(status) LIKE upper(:status)",
            countQuery = "SELECT COUNT(*) FROM department " +
                    "WHERE TO_CHAR(department_id,'999999999' ) LIKE :id " +
                    "OR TO_CHAR(department_code,'999999999' ) LIKE upper(:departmentCode) " +
                    "OR upper(department_name) LIKE upper(:departmentName) " +
                    "OR upper(status) LIKE upper(:status)",
            nativeQuery = true)
    Page<Department> searchDepartment(String id, String departmentCode, String departmentName, String status, Pageable pageable);

    @Override
    @Query(value = "SELECT * FROM department " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<Department> getStatus(Pageable pageable);

    boolean existsById(Long id);
}
