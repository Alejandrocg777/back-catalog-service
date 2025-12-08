package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.PatmentTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypesRepository extends JpaRepository<PatmentTypes, Long> {
}
