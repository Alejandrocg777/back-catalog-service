package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {


    Optional<Area> findByDescription(String description);

    @Override
    Page<Area> findAll(Pageable pageable);

    List<Area> findByStatus(String status);

    Page<Area> findAll(Specification<Area> spec, Pageable pagingSort);
}
