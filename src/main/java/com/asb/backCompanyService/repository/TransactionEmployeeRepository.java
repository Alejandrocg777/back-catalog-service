package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.EmployeeResponseDTO;
import com.asb.backCompanyService.dto.responde.TransactionEmployeeResponseDTO;
import com.asb.backCompanyService.model.TransactionEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionEmployeeRepository extends JpaRepository<TransactionEmployee, Long> {


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.TransactionEmployeeResponseDTO(t.id, e.id,e.name,t.paymentAmount,t.date, t.observation,t.typeTransaction, t.status) " +
            "FROM TransactionEmployee t " +
            "LEFT JOIN Employee e ON t.employeeId = e.id " +
            "WHERE t.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM TransactionEmployee t " +
                    "LEFT JOIN Employee e ON t.employeeId = e.id " +
                    "WHERE t.status = 'ACTIVE'")
    Page<TransactionEmployeeResponseDTO> getStatus(Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.TransactionEmployeeResponseDTO(" +
            "t.id, " +
            "t.employeeId, " +
            "e.name, " +
            "t.paymentAmount, " +
            "t.date, " +
            "t.observation, " +
            "t.typeTransaction, " +
            "t.status) " +
            "FROM TransactionEmployee t " +
            "LEFT JOIN Employee e ON t.employeeId = e.id " +
            "WHERE (:id IS NULL OR CAST(t.id AS string) LIKE :id) " +
            "AND (:employeeName IS NULL OR UPPER(e.name) LIKE UPPER(:employeeName)) " +
            "AND (:typeTransaction IS NULL OR UPPER(t.typeTransaction) LIKE UPPER(:typeTransaction)) " +
            "AND (:paymentAmount IS NULL OR CAST(t.paymentAmount AS string) LIKE :paymentAmount) " +
            "AND (:date IS NULL OR CAST(t.date AS string) LIKE :date) " +
            "AND (:observation IS NULL OR UPPER(t.observation) LIKE UPPER(:observation)) " +
            "AND (:status IS NULL OR UPPER(t.status) LIKE UPPER(:status))",
            countQuery = "SELECT COUNT(t) " +
                    "FROM TransactionEmployee t " +
                    "LEFT JOIN Employee e ON t.employeeId = e.id " +
                    "WHERE (:id IS NULL OR CAST(t.id AS string) LIKE :id) " +
                    "AND (:employeeName IS NULL OR UPPER(e.name) LIKE UPPER(:employeeName)) " +
                    "AND (:typeTransaction IS NULL OR UPPER(t.typeTransaction) LIKE UPPER(:typeTransaction)) " +
                    "AND (:paymentAmount IS NULL OR CAST(t.paymentAmount AS string) LIKE :paymentAmount) " +
                    "AND (:date IS NULL OR CAST(t.date AS string) LIKE :date) " +
                    "AND (:observation IS NULL OR UPPER(t.observation) LIKE UPPER(:observation)) " +
                    "AND (:status IS NULL OR UPPER(t.status) LIKE UPPER(:status))")
    Page<TransactionEmployeeResponseDTO> search(
            @Param("id") String id,
            @Param("employeeName") String employeeName,
            @Param("typeTransaction") String transactionType,
            @Param("paymentAmount") String amount,
            @Param("date") String date,
            @Param("observation") String observation,
            @Param("status") String status,
            Pageable pageable);

    Page<TransactionEmployee> findAll(Specification<TransactionEmployee> spec, Pageable pagingSort);
}