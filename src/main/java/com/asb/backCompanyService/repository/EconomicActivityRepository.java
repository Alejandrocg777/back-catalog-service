package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.EconomicActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EconomicActivityRepository extends JpaRepository<EconomicActivity, Long> {

    @Query(value = "SELECT * FROM economic_activity " +
            "WHERE CAST(economic_activity_id AS char) LIKE :id " +
            "OR CAST(ciiu_code AS char) LIKE :ciiuCode " +
            "OR UPPER(description) LIKE UPPER(:description) " +
            "OR UPPER(status) LIKE UPPER(:status) ",
            countQuery = "SELECT COUNT(*) FROM economic_activity " +
                    "WHERE CAST(economic_activity_id AS char) LIKE :id " +
                    "OR CAST(ciiu_code AS char) LIKE :ciiuCode " +
                    "OR UPPER(description) LIKE UPPER(:description) " +
                    "OR UPPER(status) LIKE UPPER(:status) ",
            nativeQuery = true)
    Page<EconomicActivity> searchCiiuCode(String id, String ciiuCode, String description, String status, Pageable pageable);

    @Query(value = "SELECT * FROM economic_activity " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<EconomicActivity> getActiveCiiuCodes(Pageable pageable);

    Page<EconomicActivity> findByCiiuCodeAndDescriptionContainingIgnoreCase(Long ciiuCode, String description, Pageable pageable);


    boolean existsById(Long id);
}
