package com.asb.backCompanyService.repository.department;

import com.asb.backCompanyService.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    Page<Department> searchDepartment(String id, String departmentCode, String departmentName, String status, Pageable pageable);
    Page<Department> getStatus(Pageable pageable);
}
