package com.psi.rizerio.backend.patient.application;

import com.psi.rizerio.backend.patient.application.dto.PatientRequestDTO;
import com.psi.rizerio.backend.patient.application.dto.PatientResponseDTO;
import com.psi.rizerio.backend.patient.application.mapper.PatientMapper;
import com.psi.rizerio.backend.patient.domain.Patient;
import com.psi.rizerio.backend.patient.infrastructure.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;
    private final com.psi.rizerio.backend.auth.domain.UserRepository userRepository;

    public PatientResponseDTO createPatient(PatientRequestDTO request) {
        Patient patient = mapper.toEntity(request);
        Patient savedPatient = repository.save(patient);
        return mapper.toDto(savedPatient);
    }

    public PatientResponseDTO getPatientById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public PatientResponseDTO getPatientByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .map(mapper::toDto)
                .orElseGet(() -> {
                    com.psi.rizerio.backend.auth.domain.User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Patient newPatient = Patient.builder()
                            .userId(userId)
                            .name(user.getName())
                            .email(user.getEmail())
                            .phone("00000000000") // Valor padrão para evitar erro de null
                            .build();
                    Patient saved = repository.save(newPatient);
                    return mapper.toDto(saved);
                });
    }

    public List<PatientResponseDTO> getAllPatients() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO request) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        mapper.updateEntityFromDto(request, patient);
        Patient updatedPatient = repository.save(patient);
        return mapper.toDto(updatedPatient);
    }

    public void deletePatient(UUID id) {
        repository.deleteById(id);
    }
}
