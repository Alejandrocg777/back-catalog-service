package com.asb.backCompanyService.repository;


import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductResponseDTO(c.id, c.productName, c.price, c.description,c.categoryId, a.nameCategory, c.quantity, c.imgProduct, c.status, c.productStatus) " +
            "FROM Product c " +
            "INNER JOIN Category a ON c.categoryId = a.id " +
            "WHERE c.status = 'ACTIVE'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM Product c " +
                    "WHERE c.status = 'ACTIVE'")
    Page<ProductResponseDTO> getStatus(Pageable pageable);



    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.ProductResponseDTO(c.id, c.productName, c.price, c.description,c.categoryId, a.nameCategory, c.quantity, c.imgProduct, c.status, c.productStatus) " +
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
}
