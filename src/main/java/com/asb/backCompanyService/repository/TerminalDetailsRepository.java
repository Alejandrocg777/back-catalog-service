package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.TerminalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TerminalDetailsRepository extends JpaRepository<TerminalDetails, Long> {

    @Query("SELECT td.userId FROM TerminalDetails td WHERE td.userId IN :userIds")
    List<Long> findUserIdsInUse(@Param("userIds") List<Long> userIds);

    @Query("SELECT td.userId FROM TerminalDetails td " +
            "WHERE td.userId IN :userIds " +
            "AND td.terminalId != :terminalId")
    List<Long> findUserIdsInUseExcludingTerminal(@Param("userIds") List<Long> userIds,
                                                 @Param("terminalId") Long terminalId);

    // Para borrar todos los detalles de una terminal
    void deleteByTerminalId(Long terminalId);

    @Query("SELECT td FROM TerminalDetails td WHERE td.terminalId = :terminalId")
    List<TerminalDetails> findByTerminalId(@Param("terminalId") Long terminalId);
}
