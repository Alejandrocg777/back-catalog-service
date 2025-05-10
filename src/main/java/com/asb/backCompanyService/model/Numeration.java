package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "numeration")
public class Numeration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numeration_id")
    private Long id;

    @Column(name = "accounting_document_type_id")
    private Long accountingDocumentTypeId;

    @Column(name = "auth_numer")
    private String authNumer;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Column(name = "initial_number")
    private Integer initialNumber;

    @Column(name = "final_number")
    private Integer finalNumber;

    @Column(name = "current_number")
    private Integer currentNumber;

    @Column(name = "technical_key")
    private String technicalKey;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

}
