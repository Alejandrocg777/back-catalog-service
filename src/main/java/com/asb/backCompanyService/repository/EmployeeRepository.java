package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.EmployeeResponseDTO;
import com.asb.backCompanyService.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.EmployeeResponseDTO(c.id, c.name, c.phone, c.identification, i.identificationTypeId, i.name, c.address, c.date, c.email, a.id,a.description, p.id, p.description, c.baseSalary, c.status) " +
            "FROM Employee c " +
            "LEFT JOIN Area a ON c.areaId = a.id " +
            "LEFT JOIN IdentificationType  i ON i.identificationTypeId = c.identificationTypeId " +
            "LEFT JOIN Position p ON p.id = c.positionId " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Employee c " +
                    "LEFT JOIN Area a ON c.areaId = a.id " +
                    "LEFT JOIN IdentificationType  i ON i.identificationTypeId = c.identificationTypeId " +
                    "LEFT JOIN Position p ON p.id = c.positionId " +
                    "WHERE c.status = 'ACTIVE'")
    Page<EmployeeResponseDTO> getStatus(Pageable pageable);

    @Query(value =
            "SELECT " +
                    "    e.employee_id AS employeeId, " +
                    "    e.name_ AS name, " +
                    "    e.phone AS phone, " +
                    "    e.identification AS identification, " +
                    "    i.identification_type_id AS identificationTypeId, " +
                    "    i.name AS identificationTypeName, " +
                    "    e.address AS address, " +
                    "    e.hire_date AS hireDate, " +
                    "    e.`e-mail` AS email, " +
                    "    a.area_id AS areaId, " +
                    "    a.description AS areaDescription, " +
                    "    p.position_id AS positionId, " +
                    "    p.description AS positionDescription, " +
                    "    e.base_salary AS baseSalary, " +
                    "    e.status AS status, " +
                    "    COALESCE((" +
                    "        SELECT SUM(" +
                    "            CASE " +
                    "                WHEN et.transaction_type = 'ENTRADA' THEN et.amount " +
                    "                WHEN et.transaction_type IN ('PAGO', 'PRESTAMO') THEN -et.amount " +
                    "                ELSE 0 " +
                    "            END" +
                    "        ) " +
                    "        FROM employee_transaction et " +
                    "        WHERE et.employee_id = e.employee_id" +
                    "    ), 0) AS balanceToPay " +
                    "FROM employee e " +
                    "LEFT JOIN area a ON e.area_id = a.area_id " +
                    "LEFT JOIN identification_type i ON e.identification_type_id = i.identification_type_id " +
                    "LEFT JOIN position p ON e.position_id = p.position_id " +
                    "WHERE e.status = 'ACTIVE'",
            countQuery =
                    "SELECT COUNT(*) " +
                            "FROM employee e " +
                            "WHERE e.status = 'ACTIVE'",
            nativeQuery = true)
    Page<Object[]> getEmployeePaymentStatus(Pageable pageable);

    @Query(value =
            "SELECT " +
                    "    e.employee_id AS employeeId, " +
                    "    e.name_ AS name, " +
                    "    e.phone AS phone, " +
                    "    e.identification AS identification, " +
                    "    i.identification_type_id AS identificationTypeId, " +
                    "    i.name AS identificationTypeName, " +
                    "    e.address AS address, " +
                    "    e.hire_date AS hireDate, " +
                    "    e.`e-mail` AS email, " +
                    "    a.area_id AS areaId, " +
                    "    a.description AS areaDescription, " +
                    "    p.position_id AS positionId, " +
                    "    p.description AS positionDescription, " +
                    "    e.base_salary AS baseSalary, " +
                    "    e.status AS status, " +
                    "    COALESCE((" +
                    "        SELECT SUM(" +
                    "            CASE " +
                    "                WHEN et.transaction_type = 'ENTRADA' THEN et.amount " +
                    "                WHEN et.transaction_type IN ('PAGO', 'PRESTAMO') THEN -et.amount " +
                    "                ELSE 0 " +
                    "            END" +
                    "        ) " +
                    "        FROM employee_transaction et " +
                    "        WHERE et.employee_id = e.employee_id" +
                    "    ), 0) AS balanceToPay " +
                    "FROM employee e " +
                    "LEFT JOIN area a ON e.area_id = a.area_id " +
                    "LEFT JOIN identification_type i ON e.identification_type_id = i.identification_type_id " +
                    "LEFT JOIN position p ON e.position_id = p.position_id " +
                    "WHERE e.status = 'ACTIVE'",
            nativeQuery = true)
    List<Object[]> getEmployeePaymentStatusNoPage();

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.EmployeeResponseDTO(" +
            "c.id, c.name, c.phone, c.identification, " +
            "i.identificationTypeId, i.name, " +
            "c.address, c.date, c.email, " +
            "a.id, a.description, p.id, p.description, " +
            "c.baseSalary, c.status) " +
            "FROM Employee c " +
            "LEFT JOIN Area a ON c.areaId = a.id " +
            "LEFT JOIN IdentificationType i ON i.identificationTypeId = c.identificationTypeId " +
            "LEFT JOIN Position p ON p.id = c.positionId " +
            "WHERE (:id IS NULL OR CAST(c.id AS string) LIKE :id) " +
            "AND (:name IS NULL OR UPPER(c.name) LIKE UPPER(:name)) " +
            "AND (:phone IS NULL OR UPPER(c.phone) LIKE UPPER(:phone)) " +
            "AND (:identification IS NULL OR UPPER(c.identification) LIKE UPPER(:identification)) " +
            "AND (:address IS NULL OR UPPER(c.address) LIKE UPPER(:address)) " +
            "AND (:email IS NULL OR UPPER(c.email) LIKE UPPER(:email)) " +
            "AND (:status IS NULL OR UPPER(c.status) LIKE UPPER(:status)) " +
            "AND (:typeIdentificationName IS NULL OR UPPER(i.name) LIKE UPPER(:typeIdentificationName)) " +
            "AND (:areaName IS NULL OR UPPER(a.description) LIKE UPPER(:areaName)) " +
            "AND (:positionName IS NULL OR UPPER(p.description) LIKE UPPER(:positionName)) " +
            "AND (:baseSalary IS NULL OR CAST(c.baseSalary AS string) LIKE :baseSalary) ",
            countQuery = "SELECT COUNT(c) " +
                    "FROM Employee c " +
                    "LEFT JOIN Area a ON c.areaId = a.id " +
                    "LEFT JOIN IdentificationType i ON i.identificationTypeId = c.identificationTypeId " +
                    "LEFT JOIN Position p ON p.id = c.positionId " +
                    "WHERE (:id IS NULL OR CAST(c.id AS string) LIKE :id) " +
                    "AND (:name IS NULL OR UPPER(c.name) LIKE UPPER(:name)) " +
                    "AND (:phone IS NULL OR UPPER(c.phone) LIKE UPPER(:phone)) " +
                    "AND (:identification IS NULL OR UPPER(c.identification) LIKE UPPER(:identification)) " +
                    "AND (:address IS NULL OR UPPER(c.address) LIKE UPPER(:address)) " +
                    "AND (:email IS NULL OR UPPER(c.email) LIKE UPPER(:email)) " +
                    "AND (:status IS NULL OR UPPER(c.status) LIKE UPPER(:status)) " +
                    "AND (:typeIdentificationName IS NULL OR UPPER(i.name) LIKE UPPER(:typeIdentificationName)) " +
                    "AND (:areaName IS NULL OR UPPER(a.description) LIKE UPPER(:areaName)) " +
                    "AND (:positionName IS NULL OR UPPER(p.description) LIKE UPPER(:positionName)) " +
                    "AND (:baseSalary IS NULL OR CAST(c.baseSalary AS string) LIKE :baseSalary)")
    Page<EmployeeResponseDTO> search(
            @Param("id") String id,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("identification") String identification,
            @Param("address") String address,
            @Param("status") String status,
            @Param("email") String email,
            @Param("baseSalary") String baseSalary,
            @Param("typeIdentificationName") String typeIdentificationName,
            @Param("areaName") String areaName,
            @Param("positionName") String positionName,
            Pageable pageable);

    @Query(value = """
    SELECT 
        e.employee_id AS employeeId,
        e.name_ AS name,
        e.phone AS phone,
        e.identification AS identification,
        i.identification_type_id AS identificationTypeId,
        i.name AS identificationTypeName,
        e.address AS address,
        DATE_FORMAT(e.hire_date, '%d/%m/%Y') AS hireDate,
        e.`e-mail` AS email,
        a.area_id AS areaId,
        a.description AS areaDescription,
        p.position_id AS positionId,
        p.description AS positionDescription,
        e.base_salary AS baseSalary,
        e.status AS status,
        COALESCE((
            SELECT SUM(
                CASE 
                    WHEN et.transaction_type = 'ENTRADA' THEN et.amount 
                    WHEN et.transaction_type IN ('PAGO', 'PRESTAMO') THEN -et.amount 
                    ELSE 0 
                END
            )
            FROM employee_transaction et 
            WHERE et.employee_id = e.employee_id
        ), 0) AS balanceToPay
    FROM employee e
    LEFT JOIN area a ON e.area_id = a.area_id
    LEFT JOIN identification_type i ON e.identification_type_id = i.identification_type_id
    LEFT JOIN position p ON e.position_id = p.position_id
    WHERE e.status = 'ACTIVE'
      AND (:name IS NULL OR UPPER(e.name_) LIKE UPPER(:name))
      AND (:phone IS NULL OR UPPER(e.phone) LIKE UPPER(:phone))
      AND (:identification IS NULL OR UPPER(e.identification) LIKE UPPER(:identification))
      AND (:email IS NULL OR UPPER(e.`e-mail`) LIKE UPPER(:email))
      AND (:address IS NULL OR UPPER(e.address) LIKE UPPER(:address))
      AND (:identificationTypeName IS NULL OR UPPER(i.name) LIKE UPPER(:identificationTypeName))
      AND (:areaDescription IS NULL OR UPPER(a.description) LIKE UPPER(:areaDescription))
      AND (:positionDescription IS NULL OR UPPER(p.description) LIKE UPPER(:positionDescription))
      AND (:baseSalary IS NULL OR CAST(e.base_salary AS CHAR) LIKE :baseSalary)
    ORDER BY e.employee_id ASC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM employee e
    LEFT JOIN area a ON e.area_id = a.area_id
    LEFT JOIN identification_type i ON e.identification_type_id = i.identification_type_id
    LEFT JOIN position p ON e.position_id = p.position_id
    WHERE e.status = 'ACTIVE'
      AND (:name IS NULL OR UPPER(e.name_) LIKE UPPER(:name))
      AND (:phone IS NULL OR UPPER(e.phone) LIKE UPPER(:phone))
      AND (:identification IS NULL OR UPPER(e.identification) LIKE UPPER(:identification))
      AND (:email IS NULL OR UPPER(e.`e-mail`) LIKE UPPER(:email))
      AND (:address IS NULL OR UPPER(e.address) LIKE UPPER(:address))
      AND (:identificationTypeName IS NULL OR UPPER(i.name) LIKE UPPER(:identificationTypeName))
      AND (:areaDescription IS NULL OR UPPER(a.description) LIKE UPPER(:areaDescription))
      AND (:positionDescription IS NULL OR UPPER(p.description) LIKE UPPER(:positionDescription))
      AND (:baseSalary IS NULL OR CAST(e.base_salary AS CHAR) LIKE :baseSalary)
    """,
            nativeQuery = true)
    Page<Object[]> searchEmployeePayment(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("identification") String identification,
            @Param("email") String email,
            @Param("address") String address,
            @Param("identificationTypeName") String identificationTypeName,
            @Param("areaDescription") String areaDescription,
            @Param("positionDescription") String positionDescription,
            @Param("baseSalary") String baseSalary,
            Pageable pageable);


    @Query(value = "SELECT c " +
            "FROM Employee c " +
            "where c.status = 'ACTIVE' ")
    List<Employee> getAllNoPage();

}
