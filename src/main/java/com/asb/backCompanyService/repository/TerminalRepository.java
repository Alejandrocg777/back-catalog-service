package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.model.Terminal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
        COUNT(DISTINCT td.user_id) AS numberUser,
        t.status AS status,
        CASE 
            WHEN COUNT(td.user_id) > 0 THEN
                json_agg(
                    json_build_object(
                        'userId', u.user_id,
                        'userName', u.name_
                    )
                )
            ELSE '[]'
        END AS users
    FROM terminal t
    INNER JOIN numeration n ON t.numeration_id = n.numeration_id
    LEFT JOIN terminal_details td ON t.terminal_id = td.terminal_id
    LEFT JOIN user_app u ON td.user_id = u.user_id
    WHERE t.status = 'ACTIVE'
    GROUP BY t.terminal_id, t.name, t.numeration_id, n.prefix, n.initial_number, n.final_number, t.status
    ORDER BY t.terminal_id
    """,
            countQuery = """
        SELECT COUNT(DISTINCT t.terminal_id) 
        FROM terminal t
        INNER JOIN numeration n ON t.numeration_id = n.numeration_id
        WHERE t.status = 'ACTIVE'
    """,
            nativeQuery = true
    )
    Page<Object[]> findAllTerminalesPaginadoRaw(Pageable pageable);



    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.TerminalResponseDTO(" +
            "t.id, t.name, t.numerationId, n.prefix, n.initialNumber, n.finalNumber, " +
            "0L, t.status) " +
            "FROM Terminal t " +
            "INNER JOIN Numeration n ON t.numerationId = n.id " +
            "WHERE t.status = 'ACTIVE' " +
            "AND (CAST(t.id AS string) LIKE :id " +
            "OR UPPER(t.name) LIKE UPPER(:name) " +
            "OR UPPER(n.prefix) LIKE UPPER(:prefix))",
            countQuery = "SELECT COUNT(t) " +
                    "FROM Terminal t " +
                    "INNER JOIN Numeration n ON t.numerationId = n.id " +
                    "WHERE t.status = 'ACTIVE' " +
                    "AND (CAST(t.id AS string) LIKE :id " +
                    "OR UPPER(t.name) LIKE UPPER(:name) " +
                    "OR UPPER(n.prefix) LIKE UPPER(:prefix))")
    Page<TerminalResponseDTO> search(
            @Param("id") String id,
            @Param("name") String name,
            @Param("prefix") String prefix,
            Pageable pageable);

    List<Terminal>findTerminalByStatus(String status);
}
