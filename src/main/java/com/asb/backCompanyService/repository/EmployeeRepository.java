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


    @Query(value = "SELECT c " +
            "FROM Employee c " +
            "where c.status = 'ACTIVE' ")
    List<Employee> getAllNoPage();

}
