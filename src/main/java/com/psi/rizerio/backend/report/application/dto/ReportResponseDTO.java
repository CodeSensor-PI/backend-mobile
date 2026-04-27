package com.psi.rizerio.backend.report.application.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReportResponseDTO {
    private UUID id;
    private UUID patientId;
    private String aiAnalysisContent;
    private LocalDateTime generatedAt;
}
