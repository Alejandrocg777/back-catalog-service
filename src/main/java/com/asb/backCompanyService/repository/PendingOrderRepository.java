package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.PendingOrderProductDtoResponse;
import com.asb.backCompanyService.dto.responde.PendingOrderResponseDto;
import com.asb.backCompanyService.model.PendingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingOrderRepository extends JpaRepository<PendingOrder, Long> {

    Page<PendingOrder> findByStatus(String status, Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PendingOrderResponseDto(" +
            "po.id, po.billId, po.customerId, c.name, city.cityName, c.neighborhood, po.address, po.phone, " +
            "po.observations, CAST(po.date AS string), po.total, po.statusPendingOrder) " +
            "FROM PendingOrder po " +
            "INNER JOIN Client c ON po.customerId = c.id " +
            "LEFT JOIN City city ON c.cityId = city.id " +
            "WHERE po.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(po) " +
                    "FROM PendingOrder po " +
                    "WHERE po.status = 'ACTIVE'")
    Page<PendingOrderResponseDto> getActivePendingOrders(Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PendingOrderProductDtoResponse(op.id, op.productId, p.productName,op.quantity, op.unitPrice, op.total) " +
            "FROM PendingOrderDetails op " +
            "JOIN Product p ON op.productId = p.id " +
            "WHERE op.pendingOrderId = :pendingOrderId " +
            "AND p.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM PendingOrderDetails op " +
                    "JOIN Product p ON op.productId = p.id " +
                    "WHERE op.pendingOrderId = :pendingOrderId " +
                    "AND p.status = 'ACTIVE'")
    Page<PendingOrderProductDtoResponse> getActiveProductsByPendingOrder(Long pendingOrderId, Pageable pageable);

    List<PendingOrder> findByStatus(String status);
    @Query("SELECT po.id as id, po.billId as billId, po.customerId as customerId, " +
            "c.name as customerName, c.neighborhood as neighborhood, city.cityName as cityName, " +
            "po.address as address, po.phone as phone, po.observations as observations, " +
            "CAST(po.date AS string) as date, po.total as total, po.statusPendingOrder as statusOrder " +
            "FROM PendingOrder po " +
            "INNER JOIN Client c ON po.customerId = c.id " +
            "LEFT JOIN City city ON c.cityId = city.id " +
            "WHERE po.status = 'ACTIVE' " +
            "AND po.statusPendingOrder IN ('PENDIENTE', 'EN_PROCESO')")
    List<Object[]> findActiveAndPendingOrdersData();

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PendingOrderResponseDto(" +
            "po.id, po.billId, po.customerId, c.name, city.cityName, c.neighborhood, " +
            "po.address, po.phone, po.observations, CAST(po.date AS string), " +
            "po.total, po.statusPendingOrder) " +
            "FROM PendingOrder po " +
            "INNER JOIN Client c ON po.customerId = c.id " +
            "LEFT JOIN City city ON c.cityId = city.id " +
            "WHERE po.status = 'ACTIVE' " +
            "AND (CAST(po.id AS string) LIKE :id " +
            "OR UPPER(c.name) LIKE UPPER(:customerName) " +
            "OR UPPER(c.neighborhood) LIKE UPPER(:neighborhood) " +
            "OR UPPER(city.cityName) LIKE UPPER(:cityName) " +
            "OR UPPER(po.address) LIKE UPPER(:address) " +
            "OR po.phone LIKE :phone " +
            "OR UPPER(po.observations) LIKE UPPER(:observations) " +
            "OR CAST(po.total AS string) LIKE :total " +
            "OR UPPER(po.statusPendingOrder) LIKE UPPER(:statusOrder))",
            countQuery = "SELECT COUNT(po) " +
                    "FROM PendingOrder po " +
                    "INNER JOIN Client c ON po.customerId = c.id " +
                    "LEFT JOIN City city ON c.cityId = city.id " +
                    "WHERE po.status = 'ACTIVE' " +
                    "AND (CAST(po.id AS string) LIKE :id " +
                    "OR UPPER(c.name) LIKE UPPER(:customerName) " +
                    "OR UPPER(c.neighborhood) LIKE UPPER(:neighborhood) " +
                    "OR UPPER(city.cityName) LIKE UPPER(:cityName) " +
                    "OR UPPER(po.address) LIKE UPPER(:address) " +
                    "OR po.phone LIKE :phone " +
                    "OR UPPER(po.observations) LIKE UPPER(:observations) " +
                    "OR CAST(po.total AS string) LIKE :total " +
                    "OR UPPER(po.statusPendingOrder) LIKE UPPER(:statusOrder))")
    Page<PendingOrderResponseDto> searchPendingOrder(
            @Param("id") String id,
            @Param("customerName") String customerName,
            @Param("neighborhood") String neighborhood,
            @Param("cityName") String cityName,
            @Param("address") String address,
            @Param("phone") String phone,
            @Param("observations") String observations,
            @Param("total") String total,
            @Param("statusOrder") String statusOrder,
            Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.PendingOrderProductDtoResponse(" +
            "op.id, op.productId, p.productName, op.quantity, op.unitPrice, op.total) " +
            "FROM PendingOrderDetails op " +
            "JOIN Product p ON op.productId = p.id " +
            "WHERE op.pendingOrderId = :pendingOrderId " +
            "AND p.status = 'ACTIVE' " +
            "AND (CAST(op.id AS string) LIKE :id " +
            "OR UPPER(p.productName) LIKE UPPER(:productName) " +
            "OR CAST(op.quantity AS string) LIKE :quantity " +
            "OR CAST(op.unitPrice AS string) LIKE :salePrice " +
            "OR CAST(op.total AS string) LIKE :total)",
            countQuery = "SELECT COUNT(op) " +
                    "FROM PendingOrderDetails op " +
                    "JOIN Product p ON op.productId = p.id " +
                    "WHERE op.pendingOrderId = :pendingOrderId " +
                    "AND p.status = 'ACTIVE' " +
                    "AND (CAST(op.id AS string) LIKE :id " +
                    "OR UPPER(p.productName) LIKE UPPER(:productName) " +
                    "OR CAST(op.quantity AS string) LIKE :quantity " +
                    "OR CAST(op.unitPrice AS string) LIKE :salePrice " +
                    "OR CAST(op.total AS string) LIKE :total)")
    Page<PendingOrderProductDtoResponse> searchPendingOrderProducts(
            @Param("pendingOrderId") Long pendingOrderId,
            @Param("id") String id,
            @Param("productName") String productName,
            @Param("quantity") String quantity,
            @Param("salePrice") String salePrice,
            @Param("total") String total,
            Pageable pageable);

}