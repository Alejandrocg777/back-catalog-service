package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.PendingOrderDetailDto;
import com.asb.backCompanyService.model.PendingOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingOrderDetailRepository extends JpaRepository<PendingOrderDetails, Long> {

    List<PendingOrderDetails> findByPendingOrderId(Long pendingOrderId);
    @Query("SELECT new com.asb.backCompanyService.dto.request.PendingOrderDetailDto(" +
            "pod.productId, pod.quantity, p.productName, pod.unitPrice, pod.total) " +
            "FROM PendingOrderDetails pod " +
            "INNER JOIN Product p ON pod.productId = p.id " +
            "WHERE pod.pendingOrderId = :pendingOrderId")
    List<PendingOrderDetailDto> findDetailsByOrderId(@Param("pendingOrderId") Long pendingOrderId);
}