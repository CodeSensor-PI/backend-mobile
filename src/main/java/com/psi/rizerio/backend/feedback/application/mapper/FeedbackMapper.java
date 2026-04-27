package com.psi.rizerio.backend.feedback.application.mapper;

import com.psi.rizerio.backend.feedback.application.dto.FeedbackRequestDTO;
import com.psi.rizerio.backend.feedback.application.dto.FeedbackResponseDTO;
import com.psi.rizerio.backend.feedback.domain.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "patient.id", source = "patientId")
    Feedback toEntity(FeedbackRequestDTO dto);

    @Mapping(target = "patientId", source = "patient.id")
    FeedbackResponseDTO toDto(Feedback entity);
}
