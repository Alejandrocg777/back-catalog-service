package com.asb.backCompanyService.repository;


import com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductResponseDTO(c.id, c.productName, c.price, c.description,c.categoryId, a.nameCategory, c.quantity, c.image, c.status, c.productStatus) " +
            "FROM Product c " +
            "INNER JOIN Category a ON c.categoryId = a.id " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Product c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<ProductResponseDTO> getStatus(Pageable pageable);

    List<Product>findAllByStatus(String status);



    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductResponseDTO(c.id, c.productName, c.price, c.description,c.categoryId, a.nameCategory, c.quantity, c.image, c.status, c.productStatus) " +
            "FROM Product c " +
            "INNER JOIN Category a ON c.categoryId = a.id " +
            "WHERE c.status = 'ACTIVE'" +
            "AND (:id IS NULL OR CAST(c.id AS string) LIKE :id) " +
            "AND (:quantity IS NULL OR CAST(c.quantity AS string) LIKE :quantity) " +
            "AND (:productName IS NULL OR UPPER(c.productName) LIKE UPPER(:productName)) " +
            "AND (:productStatus IS NULL OR UPPER(c.productStatus) LIKE UPPER(:productStatus)) " +
            "AND (:description IS NULL OR UPPER(c.description) LIKE UPPER(:description)) " +
            "AND (:categoryName IS NULL OR UPPER(a.nameCategory) LIKE UPPER(:categoryName)) " +
            "AND (:price IS NULL OR STR(c.price) LIKE UPPER(:price)) ",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Product c " +
                    "INNER JOIN Category a ON c.categoryId = a.id " +
                    "WHERE c.status = 'ACTIVE'" +
                    "AND (:id IS NULL OR CAST(c.id AS string) LIKE :id) " +
                    "AND (:quantity IS NULL OR CAST(c.quantity AS string) LIKE :quantity) " +
                    "AND (:productName IS NULL OR UPPER(c.productName) LIKE UPPER(:productName)) " +
                    "AND (:productStatus IS NULL OR UPPER(c.productStatus) LIKE UPPER(:productStatus)) " +
                    "AND (:description IS NULL OR UPPER(c.description) LIKE UPPER(:description)) " +
                    "AND (:categoryName IS NULL OR UPPER(a.nameCategory) LIKE UPPER(:categoryName)) " +
                    "AND (:price IS NULL OR STR(c.price) LIKE UPPER(:price)) ")
    Page<ProductResponseDTO> search(String id, String quantity, String productName, String description, String categoryName, String price, String productStatus, Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO(p.id, p.productName, tp.purchasePrice, tp.total) " +
            "FROM Product p " +
            "JOIN TransactionProduct tp ON p.id = tp.productId " +
            "JOIN Transaction t ON tp.transactionId = t.id " +
            "WHERE t.id = :id " +
            "AND t.status = 'ACTIVE' ")
    Page<ProductOfTransactionDTO> getProductOfTransaction(Long id, Pageable pageable);


    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO(p.id, p.productName, tp.purchasePrice, tp.total) " +
            "FROM Product p " +
            "JOIN TransactionProduct tp ON p.id = tp.productId " +
            "JOIN Transaction t ON tp.transactionId = t.id " +
            "WHERE t.id = :transactionId " +
            "AND (:id IS NULL OR CAST(t.userId AS string) LIKE :id)" +
            "AND (:productName IS NULL OR UPPER(p.productName) LIKE UPPER(:productName)) " +
            "AND (:purchasePrice IS NULL OR STR(tp.purchasePrice) LIKE UPPER(:purchasePrice)) " +
            "AND (:total IS NULL OR STR(tp.total) LIKE UPPER(:total)) " +
            "AND t.status = 'ACTIVE' ")
    Page<ProductOfTransactionDTO> searchProductsTransaction(Long transactionId, String id, String productName, String purchasePrice, String total,Pageable pageable);

}
