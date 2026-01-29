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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NumerationRepository extends JpaRepository<Numeration, Long> {


    @Query(value = "SELECT n.numeration_id, n.auth_numer, n.prefix, n.start_date, " +
            "n.finish_date, n.status, n.initial_number, n.final_number, n.current_number " +
            "FROM numeration n " +
            "WHERE n.status = 'ACTIVE' " +
            "AND (CAST(:id AS TEXT) IS NULL OR CAST(n.numeration_id AS TEXT) LIKE CAST(:id AS TEXT)) " +
            "AND (CAST(:authNumber AS TEXT) IS NULL OR UPPER(n.auth_numer) LIKE UPPER(CAST(:authNumber AS TEXT))) " +
            "AND (CAST(:prefix AS TEXT) IS NULL OR UPPER(n.prefix) LIKE UPPER(CAST(:prefix AS TEXT))) " +
            "AND (CAST(:startDate AS TEXT) IS NULL OR TO_CHAR(n.start_date, 'YYYY-MM-DD') = CAST(:startDate AS TEXT)) " +  // ← CAMBIO AQUÍ
            "AND (CAST(:finishDate AS TEXT) IS NULL OR TO_CHAR(n.finish_date, 'YYYY-MM-DD') = CAST(:finishDate AS TEXT)) " +  // ← CAMBIO AQUÍ
            "AND (CAST(:initialNumber AS TEXT) IS NULL OR CAST(n.initial_number AS TEXT) LIKE CAST(:initialNumber AS TEXT)) " +
            "AND (CAST(:finalNumber AS TEXT) IS NULL OR CAST(n.final_number AS TEXT) LIKE CAST(:finalNumber AS TEXT)) " +
            "AND (CAST(:currentNumber AS TEXT) IS NULL OR CAST(n.current_number AS TEXT) LIKE CAST(:currentNumber AS TEXT)) " +
            "ORDER BY " +
            "CASE WHEN CAST(:sortBy AS TEXT) = 'id' THEN n.numeration_id END, " +
            "CASE WHEN CAST(:sortBy AS TEXT) = 'startDate' THEN n.start_date END, " +
            "CASE WHEN CAST(:sortBy AS TEXT) = 'finishDate' THEN n.finish_date END " +
            "LIMIT CAST(:limit AS INTEGER) OFFSET CAST(:offset AS INTEGER)",
            nativeQuery = true)
    List<Object[]> searchNumerationNative(
            @Param("id") String id,
            @Param("authNumber") String authNumber,
            @Param("prefix") String prefix,
            @Param("startDate") String startDate,
            @Param("finishDate") String finishDate,
            @Param("initialNumber") String initialNumber,
            @Param("finalNumber") String finalNumber,
            @Param("currentNumber") String currentNumber,
            @Param("sortBy") String sortBy,
            @Param("limit") int limit,
            @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM numeration n " +
            "WHERE n.status = 'ACTIVE' " +
            "AND (CAST(:id AS TEXT) IS NULL OR CAST(n.numeration_id AS TEXT) LIKE CAST(:id AS TEXT)) " +
            "AND (CAST(:authNumber AS TEXT) IS NULL OR UPPER(n.auth_numer) LIKE UPPER(CAST(:authNumber AS TEXT))) " +
            "AND (CAST(:prefix AS TEXT) IS NULL OR UPPER(n.prefix) LIKE UPPER(CAST(:prefix AS TEXT))) " +
            "AND (CAST(:startDate AS TEXT) IS NULL OR TO_CHAR(n.start_date, 'YYYY-MM-DD') = CAST(:startDate AS TEXT)) " +  // ← CAMBIO AQUÍ
            "AND (CAST(:finishDate AS TEXT) IS NULL OR TO_CHAR(n.finish_date, 'YYYY-MM-DD') = CAST(:finishDate AS TEXT)) " +  // ← CAMBIO AQUÍ
            "AND (CAST(:initialNumber AS TEXT) IS NULL OR CAST(n.initial_number AS TEXT) LIKE CAST(:initialNumber AS TEXT)) " +
            "AND (CAST(:finalNumber AS TEXT) IS NULL OR CAST(n.final_number AS TEXT) LIKE CAST(:finalNumber AS TEXT)) " +
            "AND (CAST(:currentNumber AS TEXT) IS NULL OR CAST(n.current_number AS TEXT) LIKE CAST(:currentNumber AS TEXT))",
            nativeQuery = true)
    Long countSearchNumerationNative(
            @Param("id") String id,
            @Param("authNumber") String authNumber,
            @Param("prefix") String prefix,
            @Param("startDate") String startDate,
            @Param("finishDate") String finishDate,
            @Param("initialNumber") String initialNumber,
            @Param("finalNumber") String finalNumber,
            @Param("currentNumber") String currentNumber);

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
