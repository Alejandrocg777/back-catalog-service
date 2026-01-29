package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto;
import com.asb.backCompanyService.model.OrderAllocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderAllocationRepository extends JpaRepository<OrderAllocation, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto(" +
            "oa.id, oa.transporterId, u.name, oa.pendingOrderId, " +
            "oa.originWarehouseId, w.warehouseName, " +
            "oa.destinationNeighborhoodId, rn.neighborhood, " +
            "oa.date, oa.hour, oa.chargeInvoice, oa.address, oa.phone, " +
            "oa.observations, oa.total, oa.status, oa.image, oa.signature, oa.statusOrderAllocation, " +
            "oa.createdAt, oa.updatedAt, po.customerId, c.name, po.paymentMethodId, m.description) " +
            "FROM OrderAllocation oa " +
            "LEFT JOIN User u ON oa.transporterId = u.id " +
            "LEFT JOIN SupplierRate sr ON oa.originWarehouseId = sr.id " +
            "LEFT JOIN Supplier s ON sr.supplierId = s.id " +
            "LEFT JOIN Warehouse w ON s.warehouseId = w.id " +
            "LEFT JOIN RateNeighborhood rn ON oa.destinationNeighborhoodId = rn.id " +
            "LEFT JOIN PendingOrder po ON oa.pendingOrderId = po.id " +
            "LEFT JOIN PaymentMethod m ON po.paymentMethodId = m.id " +
            "LEFT JOIN Client c ON po.customerId = c.id " +
            "WHERE (oa.status = 'ACTIVE' OR oa.status IS NULL) " +
            "AND (oa.statusOrderAllocation = 'ASIGNADO' OR oa.statusOrderAllocation = 'EN PROCESO' OR oa.statusOrderAllocation = 'RECHAZADO') " +
            "AND CAST(oa.id AS string) LIKE :id " +
            "AND UPPER(COALESCE(u.name, '')) LIKE UPPER(:userName) " +
            "AND UPPER(COALESCE(w.warehouseName, '')) LIKE UPPER(:warehouseName) " +
            "AND UPPER(COALESCE(rn.neighborhood, '')) LIKE UPPER(:neighborhoodName) " +
            "AND UPPER(oa.address) LIKE UPPER(:address) " +
            "AND UPPER(COALESCE(m.description, '')) LIKE UPPER(:paymentMethodName) " +
            "AND (oa.date = COALESCE(:date, oa.date)) " +
            "AND UPPER(COALESCE(c.name, '')) LIKE UPPER(:customerName) " +
            "AND UPPER(oa.statusOrderAllocation) LIKE UPPER(:statusOrder)",
            countQuery = "SELECT COUNT(oa) " +
                    "FROM OrderAllocation oa " +
                    "LEFT JOIN User u ON oa.transporterId = u.id " +
                    "LEFT JOIN SupplierRate sr ON oa.originWarehouseId = sr.id " +
                    "LEFT JOIN Supplier s ON sr.supplierId = s.id " +
                    "LEFT JOIN Warehouse w ON s.warehouseId = w.id " +
                    "LEFT JOIN RateNeighborhood rn ON oa.destinationNeighborhoodId = rn.id " +
                    "LEFT JOIN PendingOrder po ON oa.pendingOrderId = po.id " +
                    "LEFT JOIN PaymentMethod m ON po.paymentMethodId = m.id " +
                    "LEFT JOIN Client c ON po.customerId = c.id " +
                    "WHERE (oa.status = 'ACTIVE' OR oa.status IS NULL) " +
                    "AND (oa.statusOrderAllocation = 'ASIGNADO' OR oa.statusOrderAllocation = 'EN PROCESO' OR oa.statusOrderAllocation = 'RECHAZADO') " +
                    "AND CAST(oa.id AS string) LIKE :id " +
                    "AND UPPER(COALESCE(u.name, '')) LIKE UPPER(:userName) " +
                    "AND UPPER(COALESCE(w.warehouseName, '')) LIKE UPPER(:warehouseName) " +
                    "AND UPPER(COALESCE(rn.neighborhood, '')) LIKE UPPER(:neighborhoodName) " +
                    "AND UPPER(oa.address) LIKE UPPER(:address) " +
                    "AND UPPER(COALESCE(m.description, '')) LIKE UPPER(:paymentMethodName) " +
                    "AND (oa.date = COALESCE(:date, oa.date)) " +
                    "AND UPPER(COALESCE(c.name, '')) LIKE UPPER(:customerName) " +
                    "AND UPPER(oa.statusOrderAllocation) LIKE UPPER(:statusOrder)")
    Page<OrderAllocationResponseDto> searchOrderAllocation(
            @Param("id") String id,
            @Param("userName") String userName,
            @Param("warehouseName") String warehouseName,
            @Param("neighborhoodName") String neighborhoodName,
            @Param("address") String address,
            @Param("paymentMethodName") String paymentMethodName,
            @Param("date") LocalDate date,
            @Param("customerName") String customerName,
            @Param("statusOrder") String statusOrder,
            Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto(" +
            "oa.id, oa.transporterId, u.name, oa.pendingOrderId, " +
            "oa.originWarehouseId, w.warehouseName, " +
            "oa.destinationNeighborhoodId, rn.neighborhood, " +
            "oa.date, oa.hour, oa.chargeInvoice, oa.address, oa.phone, " +
            "oa.observations, oa.total, oa.status, oa.image, oa.signature, oa.statusOrderAllocation, " +
            "oa.createdAt, oa.updatedAt, po.customerId, c.name, po.paymentMethodId, m.description) " +
            "FROM OrderAllocation oa " +
            "LEFT JOIN User u ON oa.transporterId = u.id " +
            "LEFT JOIN SupplierRate sr ON oa.originWarehouseId = sr.id " +
            "LEFT JOIN Supplier s ON sr.supplierId = s.id " +
            "LEFT JOIN Warehouse w ON s.warehouseId = w.id " +
            "LEFT JOIN RateNeighborhood rn ON oa.destinationNeighborhoodId = rn.id " +
            "LEFT JOIN PendingOrder po ON oa.pendingOrderId = po.id " +
            "LEFT JOIN PaymentMethod m ON po.paymentMethodId = m.id " +
            "LEFT JOIN Client c ON po.customerId = c.id " +
            "WHERE (oa.status = 'ACTIVE' OR oa.status IS NULL) " +
            "AND (oa.statusOrderAllocation = 'ASIGNADO' OR oa.statusOrderAllocation = 'EN PROCESO'  OR oa.statusOrderAllocation = 'RECHAZADO')",
            countQuery = "SELECT COUNT(oa) " +
                    "FROM OrderAllocation oa " +
                    "WHERE (oa.status = 'ACTIVE' OR oa.status IS NULL) " +
                    "AND (oa.statusOrderAllocation = 'ASIGNADO' OR oa.statusOrderAllocation = 'EN PROCESO' OR oa.statusOrderAllocation = 'RECHAZADO')")
    Page<OrderAllocationResponseDto> getActiveOrders(Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto(" +
            "oa.id, oa.transporterId, u.name, oa.pendingOrderId, " +
            "oa.originWarehouseId, w.warehouseName, " +
            "oa.destinationNeighborhoodId, rn.neighborhood, " +
            "oa.date, oa.hour, oa.chargeInvoice, oa.address, oa.phone, " +
            "oa.observations, oa.total, oa.status, oa.image, oa.signature, oa.statusOrderAllocation, " +
            "oa.createdAt, oa.updatedAt, po.customerId, c.name, po.paymentMethodId, m.description) " +
            "FROM OrderAllocation oa " +
            "LEFT JOIN User u ON oa.transporterId = u.id " +
            "LEFT JOIN SupplierRate sr ON oa.originWarehouseId = sr.id " +
            "LEFT JOIN Supplier s ON sr.supplierId = s.id " +
            "LEFT JOIN Warehouse w ON s.warehouseId = w.id " +
            "LEFT JOIN RateNeighborhood rn ON oa.destinationNeighborhoodId = rn.id " +
            "LEFT JOIN PendingOrder po ON oa.pendingOrderId = po.id " +
            "LEFT JOIN PaymentMethod m ON po.paymentMethodId = m.id " +
            "LEFT JOIN Client c ON po.customerId = c.id " +
            "WHERE (oa.status = 'ACTIVE' OR oa.status IS NULL) " +
            "AND (oa.statusOrderAllocation = 'ENTREGADO' )",
            countQuery = "SELECT COUNT(oa) " +
                    "FROM OrderAllocation oa " +
                    "WHERE (oa.status = 'ACTIVE' OR oa.status IS NULL) " +
                    "AND (oa.statusOrderAllocation = 'ENTREGADO' )")
    Page<OrderAllocationResponseDto> getActiveOrderComplete(Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto(" +
            "oa.id, oa.transporterId, u.name, oa.pendingOrderId, " +
            "oa.originWarehouseId, w.warehouseName, " +
            "oa.destinationNeighborhoodId, rn.neighborhood, " +
            "oa.date, oa.hour, oa.chargeInvoice, oa.address, oa.phone, " +
            "oa.observations, oa.total, oa.status, oa.image, oa.signature, oa.statusOrderAllocation, " +
            "oa.createdAt, oa.updatedAt, po.customerId, c.name, po.paymentMethodId, m.description) " +
            "FROM OrderAllocation oa " +
            "LEFT JOIN User u ON oa.transporterId = u.id " +
            "LEFT JOIN SupplierRate sr ON oa.originWarehouseId = sr.id " +
            "LEFT JOIN Supplier s ON sr.supplierId = s.id " +
            "LEFT JOIN Warehouse w ON s.warehouseId = w.id " +
            "LEFT JOIN RateNeighborhood rn ON oa.destinationNeighborhoodId = rn.id " +
            "LEFT JOIN PendingOrder po ON oa.pendingOrderId = po.id " +
            "LEFT JOIN PaymentMethod m ON po.paymentMethodId = m.id " +
            "LEFT JOIN Client c ON po.customerId = c.id " +
            "WHERE oa.status = 'ACTIVE' OR oa.status IS NULL")
    List<OrderAllocationResponseDto> getAllOrderAllocations();

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto(" +
            "oa.id, oa.transporterId, u.name, oa.pendingOrderId, " +
            "oa.originWarehouseId, w.warehouseName, " +
            "oa.destinationNeighborhoodId, rn.neighborhood, " +
            "oa.date, oa.hour, oa.chargeInvoice, oa.address, oa.phone, " +
            "oa.observations, oa.total, oa.status, oa.image, oa.signature, oa.statusOrderAllocation, " +
            "oa.createdAt, oa.updatedAt, po.customerId, c.name, po.paymentMethodId, m.description) " +
            "FROM OrderAllocation oa " +
            "LEFT JOIN User u ON oa.transporterId = u.id " +
            "LEFT JOIN SupplierRate sr ON oa.originWarehouseId = sr.id " +
            "LEFT JOIN Supplier s ON sr.supplierId = s.id " +
            "LEFT JOIN Warehouse w ON s.warehouseId = w.id " +
            "LEFT JOIN RateNeighborhood rn ON oa.destinationNeighborhoodId = rn.id " +
            "LEFT JOIN PendingOrder po ON oa.pendingOrderId = po.id " +
            "LEFT JOIN PaymentMethod m ON po.paymentMethodId = m.id " +
            "LEFT JOIN Client c ON po.customerId = c.id " +
            "WHERE oa.transporterId = :transporterId " +
            "AND oa.status = 'ACTIVE' " +
            "AND (oa.statusOrderAllocation = 'ASIGNADO' OR oa.statusOrderAllocation = 'EN PROCESO')")
    List<OrderAllocationResponseDto> getOrderAllocationsByTransporter(@Param("transporterId") Long transporterId);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto(" +
            "oa.id, oa.transporterId, u.name, oa.pendingOrderId, " +
            "oa.originWarehouseId, w.warehouseName, " +
            "oa.destinationNeighborhoodId, rn.neighborhood, " +
            "oa.date, oa.hour, oa.chargeInvoice, oa.address, oa.phone, " +
            "oa.observations, oa.total, oa.status, oa.image, oa.signature, oa.statusOrderAllocation, " +
            "oa.createdAt, oa.updatedAt, po.customerId, c.name, po.paymentMethodId, m.description) " +
            "FROM OrderAllocation oa " +
            "LEFT JOIN User u ON oa.transporterId = u.id " +
            "LEFT JOIN SupplierRate sr ON oa.originWarehouseId = sr.id " +
            "LEFT JOIN Supplier s ON sr.supplierId = s.id " +
            "LEFT JOIN Warehouse w ON s.warehouseId = w.id " +
            "LEFT JOIN RateNeighborhood rn ON oa.destinationNeighborhoodId = rn.id " +
            "LEFT JOIN PendingOrder po ON oa.pendingOrderId = po.id " +
            "LEFT JOIN PaymentMethod m ON po.paymentMethodId = m.id " +
            "LEFT JOIN Client c ON po.customerId = c.id " +
            "WHERE oa.transporterId = :transporterId " +
            "AND oa.status = 'ACTIVE' " +
            "AND (oa.statusOrderAllocation = 'ENTREGADO')")
    List<OrderAllocationResponseDto> getOrderComplete(@Param("transporterId") Long transporterId);

    boolean existsById(Long id);
}