package com.psi.rizerio.backend.report.domain;

import com.psi.rizerio.backend.patient.domain.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String aiAnalysisContent;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}
