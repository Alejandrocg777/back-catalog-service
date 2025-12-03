package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.ClientResponseDTO;
import com.asb.backCompanyService.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ClientResponseDTO(c.id, c.name, c.phone, c.identification, i.name, c.address, c.neighborhood, c.email, t.cityName, c.status) " +
            "FROM Client c " +
            "LEFT JOIN City t ON c.cityId = t.id " +
            "LEFT JOIN IdentificationType  i ON i.identificationTypeId = c.identificationTypeId " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Client c " +
                    "INNER JOIN City t ON c.cityId = t.id " +
                    "LEFT JOIN IdentificationType  i ON i.identificationTypeId = c.identificationTypeId " +
                    "WHERE c.status = 'ACTIVE'")
    Page<ClientResponseDTO> getStatus(Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ClientResponseDTO(c.id, c.name, c.phone, c.identification, i.name, c.address, c.neighborhood, c.email, t.cityName, c.status) " +
            "FROM Client c " +
            "INNER JOIN City t ON c.cityId = t.id " +
            "LEFT JOIN IdentificationType  i ON i.identificationTypeId = c.identificationTypeId " +
            "WHERE CAST(c.id AS string) LIKE :id " +
            "OR UPPER(c.name) LIKE :name " +
            "OR UPPER(c.phone) LIKE :phone " +
            "OR UPPER(c.identification) LIKE :identification " +
            "OR UPPER(c.address) LIKE :address " +
            "OR UPPER(c.neighborhood) LIKE :neighborhood " +
            "OR UPPER(c.email) LIKE :email " +
            "OR UPPER(t.cityName) LIKE :cityName " +
            "OR UPPER(c.status) LIKE :status " ,
            countQuery = "SELECT COUNT(*) " +
                    "FROM Client c " +
                    "INNER JOIN City t ON c.cityId = t.id " +
                    "WHERE CAST(c.id AS string) LIKE :id " +
                    "OR UPPER(c.name) LIKE :name " +
                    "OR UPPER(c.phone) LIKE :phone " +
                    "OR UPPER(c.identification) LIKE :identification " +
                    "OR UPPER(c.address) LIKE :address " +
                    "OR UPPER(c.neighborhood) LIKE :neighborhood " +
                    "OR UPPER(c.email) LIKE :email " +
                    "OR UPPER(t.cityName) LIKE :cityName " +
                    "OR UPPER(c.status) LIKE :status ")
            Page<ClientResponseDTO> searchClient(String id,
                                                 String name,
                                                 String phone,
                                                 String identification,
                                                 String address,
                                                 String cityName,
                                                 String status,
                                                 String neighborhood,
                                                 String email,
                                                 Pageable pageable);


    @Query(value = "SELECT c " +
            "FROM Client c " +
            "where c.status = 'ACTIVE' ")
    List<Client> getAllNoPage();

}
