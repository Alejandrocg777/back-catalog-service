package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.CategoryResponseDto;
import com.asb.backCompanyService.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.CategoryResponseDto(" +
            "c.id, c.nameCategory, c.soldOutValue, c.fewUnits, c.status, c.image) " +
            "FROM Category c " +
            "WHERE c.status = 'ACTIVE' " +
            "AND (CAST(c.id AS string) LIKE :id " +
            "OR UPPER(c.nameCategory) LIKE UPPER(:nameCategory) " +
            "OR CAST(c.soldOutValue AS string) LIKE :soldOutValue " +
            "OR CAST(c.fewUnits AS string) LIKE :fewUnits)",
            countQuery = "SELECT COUNT(c) " +
                    "FROM Category c " +
                    "WHERE c.status = 'ACTIVE' " +
                    "AND (CAST(c.id AS string) LIKE :id " +
                    "OR UPPER(c.nameCategory) LIKE UPPER(:nameCategory) " +
                    "OR CAST(c.soldOutValue AS string) LIKE :soldOutValue " +
                    "OR CAST(c.fewUnits AS string) LIKE :fewUnits)")
    Page<CategoryResponseDto> search(
            @Param("id") String id,
            @Param("nameCategory") String nameCategory,
            @Param("soldOutValue") String soldOutValue,
            @Param("fewUnits") String fewUnits,
            Pageable pageable);

    @Query(value = "SELECT  new com.asb.backCompanyService.dto.responde.CategoryResponseDto(c.id, c.nameCategory,c.soldOutValue,c.fewUnits, c.status, c.image) " +
            "FROM Category c WHERE c.status = 'ACTIVE'")
    Page<CategoryResponseDto> getActiveCategories(Pageable pageable);

    List<Category> findByStatus(String status);


    Page<Category> findAll(Specification<Category> spec, Pageable pagingSort);
}
