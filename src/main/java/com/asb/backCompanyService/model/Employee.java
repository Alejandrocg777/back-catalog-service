package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(name = "name_")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "identification")
    private String identification;

    @Column(name = "address")
    private String address;

    @Column(name = "e-mail")
    private String email;

    @Column(name = "identification_type_id")
    private Long identificationTypeId;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "hire_date")
    private String date;

    @Column(name = "base_salary", nullable = true)
    private Double baseSalary;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



}
