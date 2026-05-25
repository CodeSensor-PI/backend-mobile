package com.psi.rizerio.backend.feedback.application.mapper;

import com.psi.rizerio.backend.feedback.application.dto.FeedbackRequestDTO;
import com.psi.rizerio.backend.feedback.application.dto.FeedbackResponseDTO;
import com.psi.rizerio.backend.feedback.domain.Feedback;
import com.psi.rizerio.backend.patient.domain.Patient;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T19:45:13-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FeedbackMapperImpl implements FeedbackMapper {

    @Override
    public Feedback toEntity(FeedbackRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Feedback.FeedbackBuilder feedback = Feedback.builder();

        feedback.patient( feedbackRequestDTOToPatient( dto ) );
        feedback.content( dto.getContent() );
        feedback.moodScore( dto.getMoodScore() );
        feedback.sessaoId( dto.getSessaoId() );

        return feedback.build();
    }

    @Override
    public FeedbackResponseDTO toDto(Feedback entity) {
        if ( entity == null ) {
            return null;
        }

        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();

        feedbackResponseDTO.setPatientId( entityPatientId( entity ) );
        feedbackResponseDTO.setContent( entity.getContent() );
        feedbackResponseDTO.setCreatedAt( entity.getCreatedAt() );
        feedbackResponseDTO.setId( entity.getId() );
        feedbackResponseDTO.setMoodScore( entity.getMoodScore() );
        feedbackResponseDTO.setSessaoId( entity.getSessaoId() );

        return feedbackResponseDTO;
    }

    protected Patient feedbackRequestDTOToPatient(FeedbackRequestDTO feedbackRequestDTO) {
        if ( feedbackRequestDTO == null ) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        patient.id( feedbackRequestDTO.getPatientId() );

        return patient.build();
    }

    private UUID entityPatientId(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }
        Patient patient = feedback.getPatient();
        if ( patient == null ) {
            return null;
        }
        UUID id = patient.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
