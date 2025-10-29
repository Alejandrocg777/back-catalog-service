package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.dto.responde.TransactionResponseDTO;
import com.asb.backCompanyService.dto.responde.TransactionResponseNewDTO;
import com.asb.backCompanyService.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.TransactionResponseDTO(t.id, p.productName, t.transactionType, p.quantity, t.transactionDate, r.name, u.name, p.price, t.status, t.observation) " +
            "FROM Transaction t " +
            "JOIN TransactionProduct tp ON tp.transactionId = t.id " +
            "JOIN Product p ON p.id = tp.productId " +
            "LEFT JOIN User u ON t.userId = u.id " +
            "LEFT JOIN Rol r ON r.id = u.rolId " +
            "WHERE t.status = 'ACTIVE' ")
    Page<TransactionResponseDTO> getActiveTransactions(Pageable pageable);



    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.TransactionResponseDTO(t.id, p.productName, t.transactionType, p.quantity, t.transactionDate, r.name, u.name, p.price, t.status, t.observation) " +
            "FROM Transaction t " +
            "JOIN TransactionProduct tp ON tp.transactionId = t.id " +
            "JOIN Product p ON tp.productId = p.id " +
            "JOIN User u ON t.userId = u.id " +
            "JOIN Rol r ON r.id = u.rolId " +
            "WHERE (:id IS NULL OR CAST(t.id AS string) LIKE :id) " +
            "AND (:productName IS NULL OR UPPER(p.productName) LIKE UPPER(:productName)) " +
            "AND (:transactionType IS NULL OR UPPER(t.transactionType) LIKE UPPER(:transactionType)) " +
            "AND (:quantity IS NULL OR CAST(p.quantity AS string) LIKE :quantity) " +
            "AND (:typeUser IS NULL OR UPPER(r.name) LIKE UPPER(:typeUser)) " +
            "AND (:userName IS NULL OR UPPER(u.name) LIKE UPPER(:userName)) " +
            "AND (:value IS NULL OR STR(p.price) LIKE UPPER(:value)) " +
            "AND (:status IS NULL OR UPPER(t.status) LIKE UPPER(:status))")
    Page<TransactionResponseDTO> search(String id, String productName, String transactionType, String quantity, String typeUser, String userName, String value, String status, Pageable pageable);



    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.TransactionResponseNewDTO(r.name, u.name, t.transactionDate, t.transactionType, t.observation) " +
            "FROM User u " +
            "JOIN Rol r ON u.rolId = r.id " +
            "JOIN Transaction t ON u.id = t.userId " +
            "WHERE  (:transactionType IS NULL OR UPPER(t.transactionType) LIKE UPPER(:transactionType)) " +
            "AND (:observation IS NULL OR UPPER(t.observation) LIKE UPPER(:observation)) " +
            "AND (:typeUser IS NULL OR UPPER(r.name) LIKE UPPER(:typeUser)) " +
            "AND (:userName IS NULL OR UPPER(u.name) LIKE UPPER(:userName)) ")
    Page<TransactionResponseNewDTO> searchTransaction(String transactionType, String typeUser, String userName, String observation,Pageable pageable);



    @Query(value = "SELECT new com.asb.backCompanyService.dto.responde.TransactionResponseNewDTO(r.name, u.name, t.transactionDate, t.transactionType, t.observation) " +
            "FROM User u " +
            "JOIN Rol r ON u.rolId = r.id " +
            "JOIN Transaction t ON u.id = t.userId " +
            "WHERE t.status = 'ACTIVE' ")
    Page<TransactionResponseNewDTO> getActiveTransactionsMaster(Pageable pageable);

}
