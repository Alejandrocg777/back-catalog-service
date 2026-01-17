package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto;
import com.asb.backCompanyService.dto.responde.InvoiceResponseDto;
import com.asb.backCompanyService.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("""
        SELECT new com.asb.backCompanyService.dto.responde.InvoiceResponseDto(
            b.id,
            b.customerId,
            c.name,
            b.invoiceNumber,
            b.invoiceDate,
            b.dueDate,
            b.paymentTypeId,
            b.paymentMethodId,
            pm.description,
            b.deliveryType,
            b.deliveryCost,
            b.observations,
            b.totalDiscount,
            b.total,
            b.subtotal,
            b.initialPayment,
            b.remainingBalance,
            b.cashReceived,
            b.userId,
            u.name,
            b.changeGiven,
            b.status,
            b.statusBill
        )
        FROM Bill b
        LEFT JOIN Client c ON c.id = b.customerId
        LEFT JOIN PaymentMethod pm ON pm.id = b.paymentMethodId
        LEFT JOIN User u ON u.id = b.userId
        WHERE b.status = 'ACTIVE'
    """)
    Page<InvoiceResponseDto> findAllWithDetails(Pageable pageable);


    @Query("""
        SELECT new com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto(
            b.id,
            b.customerId,
            COALESCE(c.name, ''),
            b.address,
            b.phone,
            COALESCE(c.neighborhood, ''),
            COALESCE(c.identification, ''),
            b.invoiceNumber,
            b.invoiceDate,
            b.dueDate,
            b.paymentTypeId,
            b.paymentMethodId,
            COALESCE(pm.description, ''),
            b.deliveryType,
            b.deliveryCost,
            b.observations,
            b.totalDiscount,
            b.total,
            b.subtotal,
            b.initialPayment,
            b.remainingBalance,
            b.cashReceived,
            b.userId,
            COALESCE(u.name, ''),
            b.changeGiven,
            b.status,
            b.statusBill
        )
         FROM Bill b
         LEFT JOIN Client c ON c.id = b.customerId
         LEFT JOIN PaymentMethod pm ON pm.id = b.paymentMethodId
         LEFT JOIN User u ON u.id = b.userId
         WHERE b.id = :id
    """)
    Optional<GenerateInvoiceResponseDto> findByIdWithNames(@Param("id") Long id);


    @Query(value = """
    SELECT new com.asb.backCompanyService.dto.responde.InvoiceResponseDto(
        b.id,
        b.customerId,
        c.name,
        b.invoiceNumber,
        b.invoiceDate,
        b.dueDate,
        b.paymentTypeId,
        b.paymentMethodId,
        pm.description,
        b.deliveryType,
        b.deliveryCost,
        b.observations,
        b.totalDiscount,
        b.total,
        b.subtotal,
        b.initialPayment,
        b.remainingBalance,
        b.cashReceived,
        b.userId,
        u.name,
        b.changeGiven,
        b.status,
        b.statusBill
    )
    FROM Bill b
    LEFT JOIN Client c ON c.id = b.customerId
    LEFT JOIN PaymentMethod pm ON pm.id = b.paymentMethodId
    LEFT JOIN User u ON u.id = b.userId
    WHERE b.status = 'ACTIVE'
    AND (CAST(b.id AS string) LIKE :id
    OR UPPER(b.invoiceNumber) LIKE UPPER(:invoiceNumber)
    OR UPPER(c.name) LIKE UPPER(:customerName)
    OR UPPER(pm.description) LIKE UPPER(:paymentMethodName)
    OR UPPER(u.name) LIKE UPPER(:userName)
    OR UPPER(b.statusBill) LIKE UPPER(:statusBill)
    OR CAST(b.total AS string) LIKE :total)
    """,
            countQuery = """
    SELECT COUNT(b)
    FROM Bill b
    LEFT JOIN Client c ON c.id = b.customerId
    LEFT JOIN PaymentMethod pm ON pm.id = b.paymentMethodId
    LEFT JOIN User u ON u.id = b.userId
    WHERE b.status = 'ACTIVE'
    AND (CAST(b.id AS string) LIKE :id
    OR UPPER(b.invoiceNumber) LIKE UPPER(:invoiceNumber)
    OR UPPER(c.name) LIKE UPPER(:customerName)
    OR UPPER(pm.description) LIKE UPPER(:paymentMethodName)
    OR UPPER(u.name) LIKE UPPER(:userName)
    OR UPPER(b.statusBill) LIKE UPPER(:statusBill)
    OR CAST(b.total AS string) LIKE :total)
    """)
    Page<InvoiceResponseDto> searchInvoices(
            @Param("id") String id,
            @Param("invoiceNumber") String invoiceNumber,
            @Param("customerName") String customerName,
            @Param("paymentMethodName") String paymentMethodName,
            @Param("userName") String userName,
            @Param("statusBill") String statusBill,
            @Param("total") String total,
            Pageable pageable
    );
}
