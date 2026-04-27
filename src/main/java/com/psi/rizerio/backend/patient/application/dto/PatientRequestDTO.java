package com.psi.rizerio.backend.patient.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private LocalDate birthDate;
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
