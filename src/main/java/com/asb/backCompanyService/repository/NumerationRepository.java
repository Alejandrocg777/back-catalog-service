package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.NumerationResponseDto;
import com.asb.backCompanyService.model.Numeration;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NumerationRepository extends JpaRepository<Numeration, Long> {


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.NumerationResponseDto(" +
            "n.id, n.authNumber, n.prefix, n.startDate, n.finishDate, n.status, " +
            "n.initialNumber, n.finalNumber, n.currentNumber) " +
            "FROM Numeration n " +
            "WHERE n.status = 'ACTIVE' " +
            "AND (CAST(n.id AS string) LIKE :id " +
            "OR UPPER(n.authNumber) LIKE UPPER(:authNumber) " +
            "OR UPPER(n.prefix) LIKE UPPER(:prefix) " +
            "OR CAST(n.initialNumber AS string) LIKE :initialNumber " +
            "OR CAST(n.finalNumber AS string) LIKE :finalNumber " +
            "OR CAST(n.currentNumber AS string) LIKE :currentNumber)",
            countQuery = "SELECT COUNT(n) " +
                    "FROM Numeration n " +
                    "WHERE n.status = 'ACTIVE' " +
                    "AND (CAST(n.id AS string) LIKE :id " +
                    "OR UPPER(n.authNumber) LIKE UPPER(:authNumber) " +
                    "OR UPPER(n.prefix) LIKE UPPER(:prefix) " +
                    "OR CAST(n.initialNumber AS string) LIKE :initialNumber " +
                    "OR CAST(n.finalNumber AS string) LIKE :finalNumber " +
                    "OR CAST(n.currentNumber AS string) LIKE :currentNumber)")
    Page<NumerationResponseDto> searchNumeration(
            @Param("id") String id,
            @Param("authNumber") String authNumber,
            @Param("prefix") String prefix,
            @Param("initialNumber") String initialNumber,
            @Param("finalNumber") String finalNumber,
            @Param("currentNumber") String currentNumber,
            Pageable pageable);

    @Query(
            value = "SELECT new com.asb.backCompanyService.dto.responde.NumerationResponseDto(n.id, n.authNumber, n.prefix, n.startDate, n.finishDate, n.status, n.initialNumber, n.finalNumber, n.currentNumber) " +
                    "FROM Numeration n " +
                    "WHERE n.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Numeration n " +
                    "WHERE n.status = 'ACTIVE'"
    )
    Page<NumerationResponseDto> getStatus(Pageable pageable);


    @Query(
            value = "SELECT new com.asb.backCompanyService.dto.responde.NumerationResponseDto(n.id, n.authNumber, n.prefix, n.startDate, n.finishDate, n.status, n.initialNumber, n.finalNumber, n.currentNumber) " +
                    "FROM Numeration n " +
                    //"JOIN AccountingDocumentType a ON n.accountingDocumentTypeId = a.id " +
                    "WHERE n.status = 'ACTIVE'"
    )
    List<NumerationResponseDto> getAllNumeration();


    @Query(value = "SELECT prefix " +
            "FROM numeration WHERE numeration_id = :id "
            , nativeQuery = true)
    String getPrefix(Long id);


    boolean existsById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT n FROM Numeration n " +
            "JOIN Terminal t ON n.id = t.numerationId " +
            // "WHERE t.userId = :userId " +
            "AND n.status = 'ACTIVE' " +
            "AND :currentDate BETWEEN n.startDate AND n.finishDate " +
            "AND n.currentNumber < n.finalNumber")
    Optional<Numeration> findActiveNumerationForType(
            @Param("currentDate") LocalDate currentDate);


    @Query("""
    SELECT n FROM Numeration n 
    INNER JOIN Terminal t ON t.numerationId = n.id 
    INNER JOIN TerminalDetails td ON td.terminalId = t.id 
    WHERE td.userId = :userId 
    AND n.status = 'ACTIVE' 
    AND :currentDate BETWEEN n.startDate AND n.finishDate 
    AND n.currentNumber < n.finalNumber
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Numeration> findActiveNumerationForUser(
            @Param("userId") Long userId,
            @Param("currentDate") LocalDate currentDate
    );
}
