package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM item_category " +
            "WHERE CAST(category_id AS char) LIKE :id " +
            "OR UPPER(category_type) LIKE UPPER(:categoryType) " +
            "OR UPPER(name) LIKE UPPER(:name) " +
            "OR UPPER(description) LIKE UPPER(:description) " +
            "OR UPPER(status) LIKE UPPER(:status) ",
            countQuery = "SELECT COUNT(*) FROM item_category " +
                    "WHERE CAST(category_id AS char) LIKE :id " +
                    "OR UPPER(category_type) LIKE UPPER(:categoryType) " +
                    "OR UPPER(name) LIKE UPPER(:name) " +
                    "OR UPPER(description) LIKE UPPER(:description) " +
                    "OR UPPER(status) LIKE UPPER(:status) ",
            nativeQuery = true)
    Page<Category> searchCategory(String id, String categoryType, String name, String description, String status, Pageable pageable);

    @Query(value = "SELECT * FROM item_category WHERE status = 'ACTIVE'", nativeQuery = true)
    Page<Category> getActiveCategories(Pageable pageable);

    Page<Category> findByCategoryTypeAndDescriptionContainingIgnoreCase(String categoryType, String description, Pageable pageable);
}
