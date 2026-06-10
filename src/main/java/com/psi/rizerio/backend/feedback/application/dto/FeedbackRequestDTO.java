package com.psi.rizerio.backend.feedback.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class FeedbackRequestDTO {
    
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    // Opcional: feedback pode ser geral (sem vínculo a uma sessão específica).
    private UUID sessaoId;

    @NotBlank(message = "Content is required")
    private String content;

    @Min(value = 1, message = "Mood score must be between 1 and 5")
    @Max(value = 5, message = "Mood score must be between 1 and 5")
    private Integer moodScore;

    private Double latitude;
    private Double longitude;
    private String locationLabel;
}

