package com.asb.backCompanyService.repository.company.mysql;

import com.asb.backCompanyService.model.Company;
import com.asb.backCompanyService.repository.company.BaseRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("mysql")
@Repository
public interface MySqlCompanyRepository extends BaseRepository<Company, Long> {

    List<Company> findByStatus(String status);

    @Query(value = "SELECT c.company_id, c.`name_`, c.nit, c.address, c.email, c.phone, e.`description`, e.ciiu_code, c.economic_activity_id " +
            "FROM company c " +
            "JOIN economic_activity e ON c.economic_activity_id = e.economic_activity_id " +
            "WHERE c.company_id = :id ", nativeQuery = true)
    List<Object[]> getCompanyIds(Long id);

    @Query(value = "SELECT c.company_id, c.`name_`, c.nit, c.address, c.email, c.phone, e.`description`, e.ciiu_code, c.economic_activity_id " +
            "FROM company c " +
            "JOIN economic_activity e ON c.economic_activity_id = e.economic_activity_id " +
            "WHERE c.status = 'ACTIVE' " , nativeQuery = true)
    Page<Object[]> getStatus(Pageable pageable);


    boolean existsById(Long id);
}
