package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PositionRepository extends JpaRepository<Position, Long> {


    @Override
    Page<Position> findAll(Pageable pageable);

    Page<Position> findAll(Specification<Position> spec, Pageable pagingSort);
}
