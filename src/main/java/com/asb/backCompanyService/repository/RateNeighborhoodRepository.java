package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.RateNeighborhoodDtoResponse;
import com.asb.backCompanyService.model.RateNeighborhood;
import com.asb.backCompanyService.model.RateNeighborhood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RateNeighborhoodRepository extends JpaRepository<RateNeighborhood, Long> {


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.RateNeighborhoodDtoResponse(" +
            "r.id, r.cityId, c.cityName, r.departmentId, d.departmentName, r.neighborhood, r.rate, r.status) " +
            "FROM RateNeighborhood r " +
            "LEFT JOIN Department d ON r.departmentId = d.id " +
            "LEFT JOIN City c ON r.cityId = c.id " +
            "WHERE r.status = 'ACTIVE' " +
            "AND (:id IS NULL OR CAST(r.id AS string) LIKE :id) " +
            "AND (:cityName IS NULL OR UPPER(c.cityName) LIKE UPPER(:cityName)) " +
            "AND (:departmentName IS NULL OR UPPER(d.departmentName) LIKE UPPER(:departmentName)) " +
            "AND (:neighborhood IS NULL OR UPPER(r.neighborhood) LIKE UPPER(:neighborhood)) " +
            "AND (:rate IS NULL OR CAST(r.rate AS string) LIKE :rate) " +
            "AND (:status IS NULL OR UPPER(r.status) LIKE UPPER(:status))",
            countQuery = "SELECT COUNT(r) " +
                    "FROM RateNeighborhood r " +
                    "LEFT JOIN Department d ON r.departmentId = d.id " +
                    "LEFT JOIN City c ON r.cityId = c.id " +
                    "WHERE r.status = 'ACTIVE' " +
                    "AND (:id IS NULL OR CAST(r.id AS string) LIKE :id) " +
                    "AND (:cityName IS NULL OR UPPER(c.cityName) LIKE UPPER(:cityName)) " +
                    "AND (:departmentName IS NULL OR UPPER(d.departmentName) LIKE UPPER(:departmentName)) " +
                    "AND (:neighborhood IS NULL OR UPPER(r.neighborhood) LIKE UPPER(:neighborhood)) " +
                    "AND (:rate IS NULL OR CAST(r.rate AS string) LIKE :rate) " +
                    "AND (:status IS NULL OR UPPER(r.status) LIKE UPPER(:status))")
    Page<RateNeighborhoodDtoResponse> searchRateNeighborhood(
            @Param("id") String id,
            @Param("cityName") String cityName,
            @Param("departmentName") String departmentName,
            @Param("neighborhood") String neighborhood,
            @Param("rate") String rate,
            @Param("status") String status,
            Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.RateNeighborhoodDtoResponse(r.id, r.cityId, c.cityName,r.departmentId,d.departmentName,r.neighborhood,r.rate, c.status) " +
            "FROM RateNeighborhood r " +
            "JOIN Department d ON r.departmentId = d.id " +
            "JOIN City c ON r.cityId = c.id " +
            "WHERE r.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM RateNeighborhood r " +
                    "JOIN Department d ON r.departmentId = d.id " +
                    "JOIN City c ON r.cityId = c.id " +
                    "WHERE r.status = 'ACTIVE'")
    Page<RateNeighborhoodDtoResponse> getStatus(Pageable pageable);


    boolean existsById(Long id);


    @Query("SELECT r FROM RateNeighborhood r " +
            "INNER JOIN RateNeighborhood n ON r.id = n.id " +
            "WHERE n.cityId = :cityId")
    List<RateNeighborhood> findByCityId(@Param("cityId") Long cityId);


}
