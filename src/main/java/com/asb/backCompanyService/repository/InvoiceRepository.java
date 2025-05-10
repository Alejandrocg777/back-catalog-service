package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Invoice; // Use the correct model (Invoice)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Profile("mysql")  // This repository is for MySQL
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByStatus(String status);

    Optional<Invoice> findById(Long id);

    Page<Invoice> findByStatus(String status, Pageable pageable);

    boolean existsById(Long id);

    @Query(value = """
            SELECT MAX(consecutive) AS max_consecutive
            FROM invoice
            """, nativeQuery = true)
    Long getMaxConsecutive();

}
