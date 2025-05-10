package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT * FROM store", nativeQuery = true)
    Page<Store> findStore(Pageable pageable);
}
