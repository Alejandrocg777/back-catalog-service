package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "total")
    private Double total;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "observation")
    private String observation;

    @Column(name = "status")
    private String status;



}
