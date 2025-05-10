package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.ClientResponseDTO;
import com.asb.backCompanyService.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ClientResponseDTO(c.id, c.name, c.phone, c.address, c.identification, t.cityName, c.status) " +
            "FROM Client c " +
            "INNER JOIN City t ON c.cityId = t.id " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Client c " +
                    "INNER JOIN City t ON c.cityId = t.id " +
                    "WHERE c.status = 'ACTIVE'")
    Page<ClientResponseDTO> getStatus(Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ClientResponseDTO(c.id, c.name, c.phone, c.identification, c.address, t.cityName, c.status) " +
            "FROM Client c " +
            "INNER JOIN City t ON c.cityId = t.id " +
            "WHERE CAST(c.id AS string) LIKE :id " +
            "OR UPPER(c.name) LIKE :name " +
            "OR UPPER(c.phone) LIKE :phone " +
            "OR UPPER(c.identification) LIKE :identification " +
            "OR UPPER(c.address) LIKE :address " +
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
                    "OR UPPER(t.cityName) LIKE :cityName " +
                    "OR UPPER(c.status) LIKE :status ")
            Page<ClientResponseDTO> searchClient(String id,
                                                 String name,
                                                 String phone,
                                                 String identification,
                                                 String address,
                                                 String cityName,
                                                 String status,
                                                 Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ClientResponseDTO(c.id, c.name, c.phone, c.identification, c.address, t.cityName, c.status) " +
            "FROM Client c " +
            "INNER JOIN City t ON c.cityId = t.id " +
            "WHERE CAST(c.id AS string) LIKE :id " +
            "OR UPPER(c.name) LIKE :name " +
            "OR UPPER(c.phone) LIKE :phone " +
            "OR UPPER(c.identification) LIKE :identification " +
            "OR UPPER(c.address) LIKE :address " +
            "OR UPPER(t.cityName) LIKE :cityName " +
            "OR UPPER(c.status) LIKE :status ")
    List<ClientResponseDTO> searchClientNoPage(String id,
                                               String name,
                                               String phone,
                                               String identification,
                                               String address,
                                               String cityName,
                                               String status);

}
