package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.ClientResponseDTO;
import com.asb.backCompanyService.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ClientResponseDTO(c.id, c.name, c.phone, c.identification, i.name, i.identificationTypeId, c.address, c.neighborhood, c.email, t.cityName, t.id, c.checkDigit,tp.description, tp.typePersonId, ta.id, ta.name, c.status, d.departmentName, d.id) " +
            "FROM Client c " +
            "LEFT JOIN City t ON c.cityId = t.id " +
            "LEFT JOIN IdentificationType  i ON i.identificationTypeId = c.identificationTypeId " +
            "LEFT JOIN TypePerson tp ON tp.typePersonId = c.typePersonId " +
            "LEFT JOIN TaxLiability ta ON ta.id = c.taxLiabilityId " +
            "LEFT JOIN Department d ON d.id = c.departmentId " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Client c " +
                    "LEFT JOIN City t ON c.cityId = t.id " +
                    "LEFT JOIN IdentificationType  i ON i.identificationTypeId = c.identificationTypeId " +
                    "WHERE c.status = 'ACTIVE'")
    Page<ClientResponseDTO> getStatus(Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ClientResponseDTO(" +
            "c.id, c.name, c.phone, c.identification, i.name, i.identificationTypeId, " +
            "c.address, c.neighborhood, c.email, t.cityName, t.id, c.checkDigit, " +
            "tp.description, tp.typePersonId, ta.id, ta.name, c.status, d.departmentName, d.id) " +
            "FROM Client c " +
            "LEFT JOIN City t ON c.cityId = t.id " +
            "LEFT JOIN IdentificationType i ON i.identificationTypeId = c.identificationTypeId " +
            "LEFT JOIN TypePerson tp ON tp.typePersonId = c.typePersonId " +
            "LEFT JOIN TaxLiability ta ON ta.id = c.taxLiabilityId " +
            "LEFT JOIN Department d ON d.id = c.departmentId " +
            "WHERE c.status = 'ACTIVE' " +
            "AND (CAST(c.id AS string) LIKE :id " +
            "OR UPPER(c.name) LIKE UPPER(:name) " +
            "OR UPPER(c.phone) LIKE UPPER(:phone) " +
            "OR UPPER(c.identification) LIKE UPPER(:identification) " +
            "OR UPPER(c.address) LIKE UPPER(:address) " +
            "OR UPPER(c.neighborhood) LIKE UPPER(:neighborhood) " +
            "OR UPPER(c.email) LIKE UPPER(:email) " +
            "OR UPPER(t.cityName) LIKE UPPER(:municipality))",
            countQuery = "SELECT COUNT(c) " +
                    "FROM Client c " +
                    "LEFT JOIN City t ON c.cityId = t.id " +
                    "LEFT JOIN IdentificationType i ON i.identificationTypeId = c.identificationTypeId " +
                    "LEFT JOIN TypePerson tp ON tp.typePersonId = c.typePersonId " +
                    "LEFT JOIN TaxLiability ta ON ta.id = c.taxLiabilityId " +
                    "LEFT JOIN Department d ON d.id = c.departmentId " +
                    "WHERE c.status = 'ACTIVE' " +
                    "AND (CAST(c.id AS string) LIKE :id " +
                    "OR UPPER(c.name) LIKE UPPER(:name) " +
                    "OR UPPER(c.phone) LIKE UPPER(:phone) " +
                    "OR UPPER(c.identification) LIKE UPPER(:identification) " +
                    "OR UPPER(c.address) LIKE UPPER(:address) " +
                    "OR UPPER(c.neighborhood) LIKE UPPER(:neighborhood) " +
                    "OR UPPER(c.email) LIKE UPPER(:email) " +
                    "OR UPPER(t.cityName) LIKE UPPER(:municipality))")
    Page<ClientResponseDTO> searchClient(
            @Param("id") String id,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("identification") String identification,
            @Param("address") String address,
            @Param("municipality") String municipality,
            @Param("neighborhood") String neighborhood,
            @Param("email") String email,
            Pageable pageable);


    @Query(value = "SELECT c " +
            "FROM Client c " +
            "where c.status = 'ACTIVE' ")
    List<Client> getAllNoPage();

}
