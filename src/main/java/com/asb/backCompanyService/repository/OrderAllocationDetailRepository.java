package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.OrderAllocationDetailDto;
import com.asb.backCompanyService.model.OrderAllocationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAllocationDetailRepository extends JpaRepository<OrderAllocationDetail, Long> {

    @Query("SELECT new com.asb.backCompanyService.dto.request.OrderAllocationDetailDto(" +
            "d.id, d.orderAllocationId, d.pendingOrderDetailId, d.productId, " +
            "p.productName, d.assignedQuantity, d.unitPrice,dt.discount, p.image, d.total) " +
            "FROM OrderAllocationDetail d " +
            "LEFT JOIN Product p ON d.productId = p.id " +
            "LEFT JOIN PendingOrderDetails dt ON dt.id = d.pendingOrderDetailId " +
            "WHERE d.orderAllocationId = :orderAllocationId")
    List<OrderAllocationDetailDto> findDetailsByOrderAllocationId(@Param("orderAllocationId") Long orderAllocationId);

    @Modifying
    @Query("DELETE FROM OrderAllocationDetail d WHERE d.orderAllocationId = :orderAllocationId")
    void deleteByOrderAllocationId(@Param("orderAllocationId") Long orderAllocationId);

    List<OrderAllocationDetail> findByOrderAllocationId(Long orderAllocationId);
}