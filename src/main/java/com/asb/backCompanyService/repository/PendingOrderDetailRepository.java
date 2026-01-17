package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.PendingOrder;
import com.asb.backCompanyService.model.PendingOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingOrderDetailRepository extends JpaRepository<PendingOrderDetails, Long> {

}
