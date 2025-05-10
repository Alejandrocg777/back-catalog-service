package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.CityDtoResponse;
import com.asb.backCompanyService.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, Long> {


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.CityDtoResponse(c.id, c.cityCode, c.cityName, d.departmentName, c.departmentId, c.status) " +
            "FROM City c " +
            "JOIN Department d ON c.departmentId = d.id " +
            "WHERE CAST(c.id AS string) LIKE :id " +
            "OR UPPER(c.cityCode) LIKE UPPER(:cityCode) " +
            "OR UPPER(c.cityName) LIKE UPPER(:cityName) " +
            "OR UPPER(d.departmentName) LIKE UPPER(:departmentName) " +
            "OR UPPER(c.status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) "
                    + "FROM City c " +
                    "JOIN Department d ON c.departmentId = d.id " +
                    "WHERE CAST(c.id AS string) LIKE :id " +
                    "OR UPPER(c.cityCode) LIKE UPPER(:cityCode) " +
                    "OR UPPER(c.cityName) LIKE UPPER(:cityName) " +
                    "OR UPPER(d.departmentName) LIKE UPPER(:departmentName) " +
                    "OR UPPER(c.status) LIKE UPPER(:status)")
    Page<CityDtoResponse> searchCity(String id, String cityCode, String cityName, String departmentName, String status, Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.CityDtoResponse(c.id, c.cityCode, c.cityName, d.departmentName, c.departmentId, c.status) " +
            "FROM City c " +
            "JOIN Department d ON c.departmentId = d.id " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM City c " +
                    "JOIN Department d ON c.departmentId = d.id " +
                    "WHERE c.status = 'ACTIVE'")
    Page<CityDtoResponse> getStatus(Pageable pageable);


    boolean existsById(Long id);
}
