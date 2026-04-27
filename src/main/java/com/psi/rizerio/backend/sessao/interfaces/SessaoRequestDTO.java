package com.psi.rizerio.backend.sessao.interfaces;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SessaoRequestDTO {
    private UUID patientId;
    private UUID psychologistId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String clinicalNotes;
}
