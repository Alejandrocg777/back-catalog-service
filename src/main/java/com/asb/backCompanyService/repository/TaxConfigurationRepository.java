package com.asb.backCompanyService.repository;


import com.asb.backCompanyService.model.TaxConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaxConfigurationRepository extends JpaRepository<TaxConfiguration, Long> {

    @Query(value = "SELECT * FROM tax_configuration " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<TaxConfiguration> getStatus(Pageable pageable);
}