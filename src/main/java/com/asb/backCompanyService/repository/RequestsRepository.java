package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Requests;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RequestsRepository extends JpaRepository<Requests, Long> {

    @Query(value = "SELECT * FROM requests " +
            "WHERE CAST(id AS char) LIKE :id " +
            "OR UPPER(requester_name) LIKE UPPER(:requesterName) " +
            "OR UPPER(area) LIKE UPPER(:area) " +
            "OR UPPER(department) LIKE UPPER(:department) " +
            "OR UPPER(request_number) LIKE UPPER(:requestNumber) " +
            "OR UPPER(status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) FROM requests " +
                    "WHERE CAST(id AS char) LIKE :id " +
                    "OR UPPER(requester_name) LIKE UPPER(:requesterName) " +
                    "OR UPPER(area) LIKE UPPER(:area) " +
                    "OR UPPER(department) LIKE UPPER(:department) " +
                    "OR UPPER(request_number) LIKE UPPER(:requestNumber) " +
                    "OR UPPER(status) LIKE UPPER(:status)",
            nativeQuery = true)
    Page<Requests> searchRequests(String id, String requesterName, String area, String department, String requestNumber, String status, Pageable pageable);

    @Query(value = "SELECT * FROM requests " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<Requests> getStatus(Pageable pageable);

    boolean existsById(Long id);
}
