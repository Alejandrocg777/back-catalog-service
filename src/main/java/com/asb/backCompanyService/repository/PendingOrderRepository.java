package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto;
import com.asb.backCompanyService.dto.responde.InvoiceResponseDto;
import com.asb.backCompanyService.model.Bill;
import com.asb.backCompanyService.model.PendingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PendingOrderRepository extends JpaRepository<PendingOrder, Long> {

}
