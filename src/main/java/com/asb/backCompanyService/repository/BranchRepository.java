package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.BranchDtoResponse;
import com.asb.backCompanyService.model.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BranchRepository extends JpaRepository<Branch, Long> {


    @Query(value =  "SELECT new com.asb.backCompanyService.dto.responde.BranchDtoResponse(b.id, b.mainBranch, b.branchName, d.departmentName, c.cityName, b.status, co.companyName) " +
            "FROM Branch b " +
            "JOIN Department d ON b.departmentId = d.id " +
            "JOIN City c ON c.id = b.cityId " +
            "JOIN Company co ON co.id = b.companyId " +
            "WHERE CAST(b.id AS STRING ) LIKE :id " +
            "OR UPPER(b.mainBranch) LIKE UPPER(:mainBranch) " +
            "OR UPPER(b.branchName) LIKE UPPER(:branchName) " +
            "OR UPPER(d.departmentName) LIKE UPPER(:departmentName) " +
            "OR UPPER(c.cityName) LIKE UPPER(:municipality) " +
            "OR UPPER(co.companyName) LIKE UPPER(:companyName) " +
            "OR UPPER(c.status) LIKE UPPER(:status)",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Branch b " +
                    "JOIN Department d ON b.departmentId = d.id " +
                    "JOIN City c ON c.id = b.cityId " +
                    "JOIN Company co ON co.id = b.companyId " +
                    "WHERE CAST(b.id AS STRING ) LIKE :id " +
                    "OR UPPER(b.mainBranch) LIKE UPPER(:mainBranch) " +
                    "OR UPPER(b.branchName) LIKE UPPER(:branchName) " +
                    "OR UPPER(d.departmentName) LIKE UPPER(:departmentName) " +
                    "OR UPPER(c.cityName) LIKE UPPER(:municipality) " +
                    "OR UPPER(co.companyName) LIKE UPPER(:companyName) " +
                    "OR UPPER(c.status) LIKE UPPER(:status)")
    Page<BranchDtoResponse> searchBranch(String id, String mainBranch, String branchName, String departmentName, String municipality, String companyName, String status, Pageable pageable);



    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.BranchDtoResponse(b.id, b.mainBranch, b.branchName, d.departmentName, c.cityName, b.status, co.companyName) " +
            "FROM Branch b " +
            "JOIN Department d ON b.departmentId = d.id " +
            "JOIN City c ON c.id = b.cityId " +
            "JOIN Company co ON co.id = b.companyId " +
            "WHERE b.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Branch b " +
                    "JOIN Department d ON b.departmentId = d.id " +
                    "JOIN City c ON c.id = b.cityId " +
                    "JOIN Company co ON co.id = b.companyId " +
                    "WHERE b.status = 'ACTIVE'")
    Page<BranchDtoResponse> getStatus(Pageable pageable);



    boolean existsById(Long id);
}
