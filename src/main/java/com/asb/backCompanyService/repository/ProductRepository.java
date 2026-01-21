package com.asb.backCompanyService.repository;


import com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductResponseDTO(c.id, c.productName, c.price, c.purchasePrice, c.description,c.categoryId, a.nameCategory, c.quantity, c.image, c.status, c.productStatus) " +
            "FROM Product c " +
            "INNER JOIN Category a ON c.categoryId = a.id " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Product c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<ProductResponseDTO> getStatus(Pageable pageable);

    List<Product>findAllByStatus(String status);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductResponseDTO(" +
            "c.id, c.productName, c.price, c.purchasePrice, c.description, " +
            "c.categoryId, a.nameCategory, c.quantity, c.image, c.status, c.productStatus) " +
            "FROM Product c " +
            "INNER JOIN Category a ON c.categoryId = a.id " +
            "WHERE c.status = 'ACTIVE' " +
            "AND (CAST(c.id AS string) LIKE :id " +
            "OR CAST(c.quantity AS string) LIKE :quantity " +
            "OR UPPER(c.productName) LIKE UPPER(:productName) " +
            "OR UPPER(c.productStatus) LIKE UPPER(:productStatus) " +
            "OR UPPER(c.description) LIKE UPPER(:description) " +
            "OR UPPER(a.nameCategory) LIKE UPPER(:categoryName) " +
            "OR CAST(c.price AS string) LIKE :price " +
            "OR CAST(c.purchasePrice AS string) LIKE :purchasePrice)",
            countQuery = "SELECT COUNT(c) " +
                    "FROM Product c " +
                    "INNER JOIN Category a ON c.categoryId = a.id " +
                    "WHERE c.status = 'ACTIVE' " +
                    "AND (CAST(c.id AS string) LIKE :id " +
                    "OR CAST(c.quantity AS string) LIKE :quantity " +
                    "OR UPPER(c.productName) LIKE UPPER(:productName) " +
                    "OR UPPER(c.productStatus) LIKE UPPER(:productStatus) " +
                    "OR UPPER(c.description) LIKE UPPER(:description) " +
                    "OR UPPER(a.nameCategory) LIKE UPPER(:categoryName) " +
                    "OR CAST(c.price AS string) LIKE :price " +
                    "OR CAST(c.purchasePrice AS string) LIKE :purchasePrice)")
    Page<ProductResponseDTO> search(
            @Param("id") String id,
            @Param("quantity") String quantity,
            @Param("productName") String productName,
            @Param("description") String description,
            @Param("categoryName") String categoryName,
            @Param("price") String price,
            @Param("purchasePrice") String purchasePrice,
            @Param("productStatus") String productStatus,
            Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO(p.id, p.productName, tp.purchasePrice, tp.quantity,tp.total) " +
            "FROM TransactionProduct tp " +
            "JOIN Product p ON tp.productId = p.id " +
            "JOIN Transaction t ON tp.transactionId = t.id " +
            "WHERE t.id = :id " +
            "AND t.status = 'ACTIVE' " ,
            countQuery = "SELECT COUNT(*) " +
            "FROM PendingOrderDetails op " +
            "JOIN Product p ON op.productId = p.id " +
            "WHERE op.pendingOrderId = :pendingOrderId " +
            "AND p.status = 'ACTIVE'")
    Page<ProductOfTransactionDTO> getProductOfTransaction(Long id, Pageable pageable);

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO(" +
            "p.id, p.productName, tp.purchasePrice, tp.quantity, tp.total) " +
            "FROM Product p " +
            "JOIN TransactionProduct tp ON p.id = tp.productId " +
            "JOIN Transaction t ON tp.transactionId = t.id " +
            "WHERE t.id = :transactionId " +
            "AND (:id IS NULL OR CAST(t.userId AS string) LIKE :id) " +
            "AND (:productName IS NULL OR UPPER(p.productName) LIKE :productName) " +
            "AND (:purchasePrice IS NULL OR CAST(tp.purchasePrice AS string) LIKE :purchasePrice) " +
            "AND (:quantity IS NULL OR CAST(tp.quantity AS string) LIKE :quantity) " +
            "AND (:total IS NULL OR CAST(tp.total AS string) LIKE :total) " +
            "AND t.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(p) " +
                    "FROM Product p " +
                    "JOIN TransactionProduct tp ON p.id = tp.productId " +
                    "JOIN Transaction t ON tp.transactionId = t.id " +
                    "WHERE t.id = :transactionId " +
                    "AND (:id IS NULL OR CAST(t.userId AS string) LIKE :id) " +
                    "AND (:productName IS NULL OR UPPER(p.productName) LIKE :productName) " +
                    "AND (:purchasePrice IS NULL OR CAST(tp.purchasePrice AS string) LIKE :purchasePrice) " +
                    "AND (:quantity IS NULL OR CAST(tp.quantity AS string) LIKE :quantity) " +
                    "AND (:total IS NULL OR CAST(tp.total AS string) LIKE :total) " +
                    "AND t.status = 'ACTIVE'")
    Page<ProductOfTransactionDTO> searchProductsTransaction(
            @Param("transactionId") Long transactionId,
            @Param("id") String id,
            @Param("productName") String productName,
            @Param("purchasePrice") String purchasePrice,
            @Param("quantity") String quantity,
            @Param("total") String total,
            Pageable pageable);
}
