package com.psi.rizerio.backend.report.infrastructure;

import com.psi.rizerio.backend.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByPatientIdOrderByGeneratedAtDesc(UUID patientId);
}
