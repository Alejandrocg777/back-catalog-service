package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
