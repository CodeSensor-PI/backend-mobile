package com.psi.rizerio.backend.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientRequest {
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String cpf;
    private String phone;
    private String emergencyContact;
    private String emergencyPhone;
    private String address;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;
}
