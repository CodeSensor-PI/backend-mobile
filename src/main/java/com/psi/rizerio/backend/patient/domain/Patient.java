package com.psi.rizerio.backend.patient.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private LocalDate birthDate;
    
    @Column(columnDefinition = "TEXT")
    private String photo;

    @Column(name = "user_id")
    private UUID userId;

    @Column(unique = true)
    private String cpf;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    private String address;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;
    private String emergencyContact;
    private String emergencyPhone;
}
