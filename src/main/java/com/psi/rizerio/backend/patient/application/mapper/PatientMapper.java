package com.psi.rizerio.backend.patient.application.mapper;

import com.psi.rizerio.backend.patient.application.dto.PatientRequestDTO;
import com.psi.rizerio.backend.patient.application.dto.PatientResponseDTO;
import com.psi.rizerio.backend.patient.domain.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Patient toEntity(PatientRequestDTO dto);
    
    PatientResponseDTO toDto(Patient entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateEntityFromDto(PatientRequestDTO dto, @MappingTarget Patient entity);
}
