package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.SellProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellProductRepository extends CrudRepository<SellProduct, Long> {

    @Query(value = "SELECT SUM(sub.total_producto) AS total_factura " +
                "FROM ( " +
                "SELECT p.total_value * s.quantity AS total_producto " +
                "FROM sell_product s " +
                "INNER JOIN product p ON s.product_id = p.product_id " +
                "WHERE s.invoice_id = :id " +
                ") AS sub ", nativeQuery = true)
    Double totalValue(@Param("id") Long id);


    @Query(value = "SELECT " +
            "SUM(total) AS total_sum " +
            "FROM sell_product " +
            "WHERE " +
            "invoice_id = :id " +
            "GROUP BY invoice_id ", nativeQuery = true)
    Double totalSum(@Param("id") Long id);


    List<SellProduct> findAllByInvoiceId(Long id);

    void deleteSellProductByInvoiceIdAndProductId(Long id, Long productId);

}
