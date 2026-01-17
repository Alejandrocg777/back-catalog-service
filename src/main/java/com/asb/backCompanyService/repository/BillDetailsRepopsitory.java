package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.request.InvoiceDetailDTO;
import com.asb.backCompanyService.model.BillDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillDetailsRepopsitory extends JpaRepository<BillDetails, Long> {

    @Query("""
        SELECT new com.asb.backCompanyService.dto.request.InvoiceDetailDTO(
            bd.id,
            bd.productId,
            COALESCE(p.productName, ''),
            bd.quantity,
            bd.unitPrice,
            bd.discountPercent,
            bd.discountFixed,
            bd.totalDiscount,
            bd.subtotal,
            bd.total
        )
        FROM BillDetails bd
        LEFT JOIN Product p ON p.id = bd.productId
        WHERE bd.facturaId = :billId
        ORDER BY bd.id ASC
    """)
    List<InvoiceDetailDTO> findDetailsByBillId(@Param("billId") Long billId);

    @Query("SELECT bd FROM BillDetails bd WHERE bd.facturaId = :billId ORDER BY bd.id ASC")
    List<BillDetails> findByFacturaId(@Param("billId") Long billId);
}