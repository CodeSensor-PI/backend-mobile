package com.psi.rizerio.backend.report.interfaces;

import com.psi.rizerio.backend.report.application.ReportService;
import com.psi.rizerio.backend.report.application.dto.ReportResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients/{patientId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<ReportResponseDTO> generateReport(@PathVariable UUID patientId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.generateReportForPatient(patientId));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getReports(@PathVariable UUID patientId) {
        return ResponseEntity.ok(reportService.getReportsByPatient(patientId));
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID patientId, @PathVariable UUID reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
