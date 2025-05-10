package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.RequestDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RequestDetailsRepository extends JpaRepository<RequestDetails, Long> {

    @Query(value = "SELECT * FROM request_details " +
            "WHERE CAST(id AS char) LIKE :id " +
            "OR UPPER(item_code) LIKE UPPER(:itemCode) " +
            "OR UPPER(description) LIKE UPPER(:description) " +
            "OR UPPER(destination) LIKE UPPER(:destination) " +
            "OR UPPER(item_type) LIKE UPPER(:itemType) " +
            "OR CAST(requested_quantity AS char) LIKE :requestedQuantity " +
            "OR UPPER(observations) LIKE UPPER(:observations) " +
            "OR UPPER(status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) FROM request_details " +
                    "WHERE CAST(id AS char) LIKE :id " +
                    "OR UPPER(item_code) LIKE UPPER(:itemCode) " +
                    "OR UPPER(description) LIKE UPPER(:description) " +
                    "OR UPPER(destination) LIKE UPPER(:destination) " +
                    "OR UPPER(item_type) LIKE UPPER(:itemType) " +
                    "OR CAST(requested_quantity AS char) LIKE :requestedQuantity " +
                    "OR UPPER(observations) LIKE UPPER(:observations) " +
                    "OR UPPER(status) LIKE UPPER(:status)",
            nativeQuery = true)
    Page<RequestDetails> searchRequestDetails(String id, String itemCode, String description, String destination, String itemType, String requestedQuantity, String observations, String status, Pageable pageable);

    @Query(value = "SELECT * FROM request_details " +
            "WHERE status = 'ACTIVE'",
            nativeQuery = true)
    Page<RequestDetails> getStatus(Pageable pageable);

    boolean existsById(Long id);
}
