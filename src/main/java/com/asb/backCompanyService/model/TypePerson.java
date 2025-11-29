package com.asb.backCompanyService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "type_person")
public class TypePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_person_id")
    private Long identificationTypeId;


    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

}
