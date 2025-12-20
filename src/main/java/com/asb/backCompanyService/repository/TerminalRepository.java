package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.model.Terminal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {


    @Query(
            value = """
        SELECT 
            t.terminal_id AS id,
            t.name AS name,
            t.numeration_id AS numerationId,
            n.prefix AS prefix,
            n.initial_number AS initialNumber,
            n.final_number AS finalNumber,
            t.user_id AS userId,
            u.name_ AS userName,
            (
                SELECT COUNT(*)
                FROM terminal t2
                WHERE t2.user_id = t.user_id
            ) AS numberUser,
            t.status AS status
        FROM terminal t
        LEFT JOIN numeration n ON t.numeration_id = n.numeration_id
        LEFT JOIN user_app u ON t.user_id = u.user_id
        where t.status = 'ACTIVE'
        ORDER BY t.terminal_id
        """,
            countQuery = "SELECT COUNT(*) FROM terminal",
            nativeQuery = true
    )
    Page<Object[]> findAllTerminalesPaginadoRaw(Pageable pageable);


    List<Terminal>findTerminalByStatus(String status);
}
