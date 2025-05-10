package com.asb.backCompanyService.repository.company;

import com.asb.backCompanyService.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    List<Company> findByStatus(String status);
    List<Object[]> getCompanyIds(Long id);
    Page<Object[]> getStatus(Pageable pageable);
}