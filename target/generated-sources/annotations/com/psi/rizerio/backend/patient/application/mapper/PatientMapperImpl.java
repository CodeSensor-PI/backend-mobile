package com.psi.rizerio.backend.patient.application.mapper;

import com.psi.rizerio.backend.patient.application.dto.PatientRequestDTO;
import com.psi.rizerio.backend.patient.application.dto.PatientResponseDTO;
import com.psi.rizerio.backend.patient.domain.Patient;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-10T12:09:20-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public Patient toEntity(PatientRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        patient.name( dto.getName() );
        patient.email( dto.getEmail() );
        patient.phone( dto.getPhone() );
        patient.birthDate( dto.getBirthDate() );
        patient.photo( dto.getPhoto() );
        patient.cpf( dto.getCpf() );
        patient.clinicalNotes( dto.getClinicalNotes() );
        patient.address( dto.getAddress() );
        patient.neighborhood( dto.getNeighborhood() );
        patient.city( dto.getCity() );
        patient.state( dto.getState() );
        patient.cep( dto.getCep() );
        patient.emergencyContact( dto.getEmergencyContact() );
        patient.emergencyPhone( dto.getEmergencyPhone() );
        patient.latitude( dto.getLatitude() );
        patient.longitude( dto.getLongitude() );

        return patient.build();
    }

    @Override
    public PatientResponseDTO toDto(Patient entity) {
        if ( entity == null ) {
            return null;
        }

        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();

        patientResponseDTO.setId( entity.getId() );
        patientResponseDTO.setName( entity.getName() );
        patientResponseDTO.setEmail( entity.getEmail() );
        patientResponseDTO.setPhone( entity.getPhone() );
        patientResponseDTO.setBirthDate( entity.getBirthDate() );
        patientResponseDTO.setUserId( entity.getUserId() );
        patientResponseDTO.setCpf( entity.getCpf() );
        patientResponseDTO.setAddress( entity.getAddress() );
        patientResponseDTO.setNeighborhood( entity.getNeighborhood() );
        patientResponseDTO.setCity( entity.getCity() );
        patientResponseDTO.setState( entity.getState() );
        patientResponseDTO.setCep( entity.getCep() );
        patientResponseDTO.setEmergencyContact( entity.getEmergencyContact() );
        patientResponseDTO.setEmergencyPhone( entity.getEmergencyPhone() );
        patientResponseDTO.setPhoto( entity.getPhoto() );
        patientResponseDTO.setClinicalNotes( entity.getClinicalNotes() );
        patientResponseDTO.setLatitude( entity.getLatitude() );
        patientResponseDTO.setLongitude( entity.getLongitude() );

        return patientResponseDTO;
    }

    @Override
    public void updateEntityFromDto(PatientRequestDTO dto, Patient entity) {
        if ( dto == null ) {
            return;
        }

        entity.setName( dto.getName() );
        entity.setEmail( dto.getEmail() );
        entity.setPhone( dto.getPhone() );
        entity.setBirthDate( dto.getBirthDate() );
        entity.setPhoto( dto.getPhoto() );
        entity.setCpf( dto.getCpf() );
        entity.setClinicalNotes( dto.getClinicalNotes() );
        entity.setAddress( dto.getAddress() );
        entity.setNeighborhood( dto.getNeighborhood() );
        entity.setCity( dto.getCity() );
        entity.setState( dto.getState() );
        entity.setCep( dto.getCep() );
        entity.setEmergencyContact( dto.getEmergencyContact() );
        entity.setEmergencyPhone( dto.getEmergencyPhone() );
        entity.setLatitude( dto.getLatitude() );
        entity.setLongitude( dto.getLongitude() );
    }
}
