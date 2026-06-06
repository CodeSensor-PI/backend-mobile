package com.psi.rizerio.backend.patient.application.mapper;

import com.psi.rizerio.backend.patient.application.dto.PatientRequestDTO;
import com.psi.rizerio.backend.patient.application.dto.PatientResponseDTO;
import com.psi.rizerio.backend.patient.domain.Patient;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T21:57:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public Patient toEntity(PatientRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        patient.address( dto.getAddress() );
        patient.birthDate( dto.getBirthDate() );
        patient.cep( dto.getCep() );
        patient.city( dto.getCity() );
        patient.clinicalNotes( dto.getClinicalNotes() );
        patient.cpf( dto.getCpf() );
        patient.email( dto.getEmail() );
        patient.emergencyContact( dto.getEmergencyContact() );
        patient.emergencyPhone( dto.getEmergencyPhone() );
        patient.latitude( dto.getLatitude() );
        patient.longitude( dto.getLongitude() );
        patient.name( dto.getName() );
        patient.neighborhood( dto.getNeighborhood() );
        patient.phone( dto.getPhone() );
        patient.photo( dto.getPhoto() );
        patient.state( dto.getState() );

        return patient.build();
    }

    @Override
    public PatientResponseDTO toDto(Patient entity) {
        if ( entity == null ) {
            return null;
        }

        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();

        patientResponseDTO.setAddress( entity.getAddress() );
        patientResponseDTO.setBirthDate( entity.getBirthDate() );
        patientResponseDTO.setCep( entity.getCep() );
        patientResponseDTO.setCity( entity.getCity() );
        patientResponseDTO.setClinicalNotes( entity.getClinicalNotes() );
        patientResponseDTO.setCpf( entity.getCpf() );
        patientResponseDTO.setEmail( entity.getEmail() );
        patientResponseDTO.setEmergencyContact( entity.getEmergencyContact() );
        patientResponseDTO.setEmergencyPhone( entity.getEmergencyPhone() );
        patientResponseDTO.setId( entity.getId() );
        patientResponseDTO.setLatitude( entity.getLatitude() );
        patientResponseDTO.setLongitude( entity.getLongitude() );
        patientResponseDTO.setName( entity.getName() );
        patientResponseDTO.setNeighborhood( entity.getNeighborhood() );
        patientResponseDTO.setPhone( entity.getPhone() );
        patientResponseDTO.setPhoto( entity.getPhoto() );
        patientResponseDTO.setState( entity.getState() );
        patientResponseDTO.setUserId( entity.getUserId() );

        return patientResponseDTO;
    }

    @Override
    public void updateEntityFromDto(PatientRequestDTO dto, Patient entity) {
        if ( dto == null ) {
            return;
        }

        entity.setAddress( dto.getAddress() );
        entity.setBirthDate( dto.getBirthDate() );
        entity.setCep( dto.getCep() );
        entity.setCity( dto.getCity() );
        entity.setClinicalNotes( dto.getClinicalNotes() );
        entity.setCpf( dto.getCpf() );
        entity.setEmail( dto.getEmail() );
        entity.setEmergencyContact( dto.getEmergencyContact() );
        entity.setEmergencyPhone( dto.getEmergencyPhone() );
        entity.setLatitude( dto.getLatitude() );
        entity.setLongitude( dto.getLongitude() );
        entity.setName( dto.getName() );
        entity.setNeighborhood( dto.getNeighborhood() );
        entity.setPhone( dto.getPhone() );
        entity.setPhoto( dto.getPhoto() );
        entity.setState( dto.getState() );
    }
}
