package com.psi.rizerio.backend.patient.application.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PatientResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private UUID userId;
    private String cpf;
    private String address;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;
    private String emergencyContact;
    private String emergencyPhone;
    private String photo;
    private String clinicalNotes;
}
