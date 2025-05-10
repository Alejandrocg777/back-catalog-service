package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.NumerationResponseDto;
import com.asb.backCompanyService.model.Numeration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NumerationRepository extends JpaRepository<Numeration, Long> {


    @Query(
            value = "SELECT new com.asb.backCompanyService.dto.responde.NumerationResponseDto(n.id, n.accountingDocumentTypeId, n.authNumer, n.prefix, n.status, n.initialNumber, n.finalNumber, n.currentNumber, a.description, n.technicalKey) " +
                    "FROM Numeration n " +
                    "JOIN AccountingDocumentType a ON n.accountingDocumentTypeId = a.id " +
                    "WHERE CAST(n.id AS string) LIKE :id " +
                    "OR UPPER(n.authNumer) LIKE UPPER(:authNumer) " +
                    "OR UPPER(n.prefix) LIKE UPPER(:prefix) " +
                    "OR UPPER(n.status) LIKE UPPER(:status) " +
                    "OR CAST(n.technicalKey AS string) LIKE :technicalKey " +
                    "OR CAST(a.description AS string) LIKE :descriptionAccountingDocumentType " +
                    "OR CAST(n.currentNumber AS string) LIKE :currentNumber",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Numeration n " +
                    "JOIN AccountingDocumentType a ON n.accountingDocumentTypeId = a.id " +
                    "WHERE CAST(n.id AS string) LIKE :id " +
                    "OR UPPER(n.authNumer) LIKE UPPER(:authNumer) " +
                    "OR UPPER(n.prefix) LIKE UPPER(:prefix) " +
                    "OR UPPER(n.status) LIKE UPPER(:status) " +
                    "OR CAST(n.technicalKey AS string) LIKE :technicalKey " +
                    "OR CAST(a.description AS string) LIKE :descriptionAccountingDocumentType " +
                    "OR CAST(n.currentNumber AS string) LIKE :currentNumber"
    )
    Page<NumerationResponseDto> searchNumeration(
            @Param("id") String id,
            @Param("authNumer") String authNumer,
            @Param("prefix") String prefix,
            @Param("status") String status,
            @Param("technicalKey") String technicalKey,
            @Param("descriptionAccountingDocumentType") String descriptionAccountingDocumentType,
            @Param("currentNumber") String currentNumber,
            Pageable pageable);


    @Query(
            value = "SELECT new com.asb.backCompanyService.dto.responde.NumerationResponseDto(n.id, n.accountingDocumentTypeId, n.authNumer, n.prefix, n.startDate, n.finishDate, n.status, n.initialNumber, n.finalNumber, n.currentNumber, a.description, n.technicalKey) " +
                    "FROM Numeration n " +
                    "JOIN AccountingDocumentType a ON n.accountingDocumentTypeId = a.id " +
                    "WHERE n.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Numeration n " +
                    "JOIN AccountingDocumentType a ON n.accountingDocumentTypeId = a.id " +
                    "WHERE n.status = 'ACTIVE'"
    )
    Page<NumerationResponseDto> getStatus(Pageable pageable);


    @Query(
            value = "SELECT new com.asb.backCompanyService.dto.responde.NumerationResponseDto(n.id, n.accountingDocumentTypeId, n.authNumer, n.prefix, n.startDate, n.finishDate, n.status, n.initialNumber, n.finalNumber, n.currentNumber, a.description, n.technicalKey) " +
                    "FROM Numeration n " +
                    "JOIN AccountingDocumentType a ON n.accountingDocumentTypeId = a.id " +
                    "WHERE n.status = 'ACTIVE'"
    )
    List<NumerationResponseDto> getAllNumeration();


    @Query(value = "SELECT prefix " +
            "FROM numeration WHERE numeration_id = :id "
            , nativeQuery = true)
    String getPrefix(Long id);


    boolean existsById(Long id);
}
