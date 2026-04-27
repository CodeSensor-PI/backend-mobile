package com.psi.rizerio.backend.report.application;

import com.psi.rizerio.backend.feedback.domain.Feedback;
import com.psi.rizerio.backend.feedback.infrastructure.FeedbackRepository;
import com.psi.rizerio.backend.patient.domain.Patient;
import com.psi.rizerio.backend.patient.infrastructure.PatientRepository;
import com.psi.rizerio.backend.report.application.dto.ReportResponseDTO;
import com.psi.rizerio.backend.report.domain.AiReportGenerator;
import com.psi.rizerio.backend.report.domain.Report;
import com.psi.rizerio.backend.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PatientRepository patientRepository;
    private final FeedbackRepository feedbackRepository;
    private final AiReportGenerator aiReportGenerator;

    public ReportResponseDTO generateReportForPatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Feedback> feedbacks = feedbackRepository.findByPatientIdOrderByCreatedAtDesc(patientId);

        if (feedbacks.isEmpty()) {
            throw new RuntimeException("Nenhum feedback encontrado para este paciente. O paciente precisa enviar um feedback de sessão antes de gerar um relatório.");
        }

        String prompt = buildPrompt(patient, feedbacks);
        String aiResponse = aiReportGenerator.generateReport(prompt);

        Report report = Report.builder()
                .patient(patient)
                .aiAnalysisContent(aiResponse)
                .build();

        Report savedReport = reportRepository.save(report);

        return ReportResponseDTO.builder()
                .id(savedReport.getId())
                .patientId(savedReport.getPatient().getId())
                .aiAnalysisContent(savedReport.getAiAnalysisContent())
                .generatedAt(savedReport.getGeneratedAt())
                .build();
    }

    public List<ReportResponseDTO> getReportsByPatient(UUID patientId) {
        return reportRepository.findByPatientIdOrderByGeneratedAtDesc(patientId).stream()
                .map(report -> ReportResponseDTO.builder()
                        .id(report.getId())
                        .patientId(report.getPatient().getId())
                        .aiAnalysisContent(report.getAiAnalysisContent())
                        .generatedAt(report.getGeneratedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteReport(UUID reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new RuntimeException("Report not found");
        }
        reportRepository.deleteById(reportId);
    }

    private String buildPrompt(Patient patient, List<Feedback> feedbacks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Você é um assistente de IA especializado em psicologia e saúde mental. ");
        sb.append("Sua tarefa é gerar um relatório clínico preliminar para um psicólogo profissional.\n\n");
        sb.append("IDENTIFICAÇÃO DO PACIENTE (USE APENAS ESTE NOME):\n");
        sb.append("Nome completo: ").append(patient.getName()).append("\n");
        if (patient.getClinicalNotes() != null) {
            sb.append("Histórico/Notas Clínicas: ").append(patient.getClinicalNotes()).append("\n");
        }
        sb.append("\nFEEDBACKS DAS SESSÕES (DADOS REAIS):\n");
        
        for (Feedback feedback : feedbacks) {
            sb.append("- Sessão em: ").append(feedback.getCreatedAt())
              .append(" | Pontuação de Humor (1-5): ").append(feedback.getMoodScore() != null ? feedback.getMoodScore() : "N/A")
              .append(" | Relato do Paciente: ").append(feedback.getContent()).append("\n");
        }

        sb.append("\nINSTRUÇÕES CRÍTICAS:\n");
        sb.append("1. NUNCA utilize nomes fictícios como 'João da Silva' ou 'Maria' a menos que sejam os nomes reais fornecidos acima.\n");
        sb.append("2. Analise os sentimentos e a evolução baseando-se EXCLUSIVAMENTE nos relatos e notas clínicas acima.\n");
        sb.append("3. Se houver poucos dados, seja breve mas preciso.\n");
        sb.append("4. Responda obrigatoriamente em PORTUGUÊS (Brasil).\n");
        sb.append("5. Mantenha um tom profissional e empático.\n\n");
        
        sb.append("Formate em Markdown com os seguintes títulos:\n");
        sb.append("### Resumo do Quadro\n");
        sb.append("### Evolução do Paciente\n");
        sb.append("### Recomendações / Alertas de Risco\n");

        return sb.toString();
    }
}
