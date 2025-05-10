package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.ItemVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {

    @Query(value = "SELECT * FROM item_variant " +
            "WHERE CAST(variant_id AS char) LIKE :id " +
            "OR UPPER(variant_type) LIKE UPPER(:variantType) " +
            "OR UPPER(name) LIKE UPPER(:name) " +
            "OR UPPER(description) LIKE UPPER(:description) " +
            "OR UPPER(status) LIKE UPPER(:status) ",
            countQuery = "SELECT COUNT(*) FROM item_variant " +
                    "WHERE CAST(variant_id AS char) LIKE :id " +
                    "OR UPPER(variant_type) LIKE UPPER(:variantType) " +
                    "OR UPPER(name) LIKE UPPER(:name) " +
                    "OR UPPER(description) LIKE UPPER(:description) " +
                    "OR UPPER(status) LIKE UPPER(:status) ",
            nativeQuery = true)
    Page<ItemVariant> searchItemVariants(String id, String variantType, String name, String description, String status, Pageable pageable);

    @Query(value = "SELECT * FROM item_variant WHERE status = 'ACTIVE'", nativeQuery = true)
    Page<ItemVariant> getActiveItemVariants(Pageable pageable);

    Page<ItemVariant> findByVariantTypeAndDescriptionContainingIgnoreCase(String variantType, String description, Pageable pageable);
}
