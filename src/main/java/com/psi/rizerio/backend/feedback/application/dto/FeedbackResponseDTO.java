package com.psi.rizerio.backend.feedback.application.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FeedbackResponseDTO {
    private UUID id;
    private UUID patientId;
    private UUID sessaoId;
    private String content;
    private Integer moodScore;
    private LocalDateTime createdAt;
}
