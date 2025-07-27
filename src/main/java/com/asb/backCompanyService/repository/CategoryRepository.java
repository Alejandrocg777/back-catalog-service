package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.CategoryDto;
import com.asb.backCompanyService.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.request.CategoryDto(" + "c.id, c.nameCategory, c.soldOutValue, c.fewUnits, c.status) " +
            "FROM Category c " +
            "WHERE c.status = 'ACTIVE' " +
            "AND (:id IS NULL OR CAST(c.id AS string) LIKE :id) " +
            "AND (:nameCategory IS NULL OR UPPER(c.nameCategory) LIKE UPPER(:nameCategory)) " +
            "AND (:soldOutValue IS NULL OR STR(c.soldOutValue) LIKE :soldOutValue)" +
            "AND (:fewUnits IS NULL OR STR(c.fewUnits) LIKE :fewUnits)" ,
            countQuery = "SELECT COUNT(c) FROM Category c " +
                    "WHERE c.status = 'ACTIVE' " +
                    "AND (:id IS NULL OR CAST(c.id AS string) LIKE :id) " +
                    "AND (:nameCategory IS NULL OR UPPER(c.nameCategory) LIKE UPPER(:nameCategory)) " +
                    "AND (:soldOutValue IS NULL OR STR(c.soldOutValue) LIKE :soldOutValue)" +
                    "AND (:fewUnits IS NULL OR STR(c.fewUnits) LIKE :fewUnits)" )
    Page<CategoryDto> search(String id, String nameCategory, String soldOutValue, String fewUnits, Pageable pageable);


    @Query(value = "SELECT  new com.asb.backCompanyService.dto.request.CategoryDto(c.id, c.nameCategory,c.soldOutValue,c.fewUnits, c.status) " +
            "FROM Category c WHERE c.status = 'ACTIVE'")
    Page<CategoryDto> getActiveCategories(Pageable pageable);

    List<Category> findByStatus(String status);



}
