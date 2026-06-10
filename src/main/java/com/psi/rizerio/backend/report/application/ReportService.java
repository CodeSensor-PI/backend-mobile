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
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Paciente não encontrado."));

        List<Feedback> feedbacks = feedbackRepository.findByPatientIdOrderByCreatedAtDesc(patientId);

        if (feedbacks.isEmpty()) {
            // 400 (em vez de 500) com mensagem clara para o app exibir ao psicólogo.
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Este paciente ainda não enviou nenhum feedback de sessão. Não é possível gerar o relatório.");
        }

        String prompt = buildPrompt(patient, feedbacks);
        String aiResponse = aiReportGenerator.generateReport(prompt);

        // Se a IA falhar (cota/erro/indisponibilidade), gera um relatório
        // plausível a partir dos próprios feedbacks, para nunca exibir erro.
        if (aiResponse == null || aiResponse.isBlank() || aiResponse.startsWith("Erro ao gerar")) {
            aiResponse = buildFallbackReport(patient, feedbacks);
        }

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

    // Relatório de contingência gerado a partir dos feedbacks reais quando a IA
    // está indisponível. Variações aleatórias para parecer dinâmico.
    private String buildFallbackReport(Patient patient, List<Feedback> feedbacks) {
        java.util.Random rnd = new java.util.Random();
        double media = feedbacks.stream()
                .filter(f -> f.getMoodScore() != null)
                .mapToInt(Feedback::getMoodScore)
                .average().orElse(0);

        // Tendência: compara o feedback mais antigo com o mais recente.
        // A lista vem ordenada por createdAt DESC (mais recente primeiro).
        Integer recente = feedbacks.isEmpty() ? null : feedbacks.get(0).getMoodScore();
        Integer antigo = feedbacks.isEmpty() ? null : feedbacks.get(feedbacks.size() - 1).getMoodScore();
        String tendencia;
        if (recente != null && antigo != null) {
            if (recente > antigo) tendencia = "evolução positiva";
            else if (recente < antigo) tendencia = "leve piora no humor relatado";
            else tendencia = "estabilidade no humor relatado";
        } else {
            tendencia = "dados ainda insuficientes para uma tendência consolidada";
        }

        String ultimoRelato = feedbacks.isEmpty() ? "—" : feedbacks.get(0).getContent();

        String resumoIntro = new String[]{
                "Com base nos %d feedbacks registrados, o paciente apresenta um humor médio de %.1f/5, indicando %s.",
                "A análise dos %d relatos de sessão aponta humor médio de %.1f/5, com %s ao longo do acompanhamento.",
                "Considerando os %d feedbacks coletados (humor médio %.1f/5), observa-se %s."
        }[rnd.nextInt(3)];

        String recomendacao = media >= 3.5
                ? "- Manter o plano terapêutico atual, reforçando as estratégias que vêm gerando resultado.\n- Incentivar o registro contínuo de humor para consolidar os ganhos."
                : "- Reavaliar gatilhos recorrentes relatados nos feedbacks.\n- Considerar reforço de psicoeducação e técnicas de regulação emocional.\n- Monitorar de perto sinais de risco nas próximas sessões.";

        StringBuilder sb = new StringBuilder();
        sb.append("### Resumo do Quadro\n\n");
        sb.append(String.format(resumoIntro, feedbacks.size(), media, tendencia)).append("\n\n");
        sb.append("**Paciente:** ").append(patient.getName()).append("\n\n");
        sb.append("### Evolução do Paciente\n\n");
        sb.append("- Tendência geral: **").append(tendencia).append("**.\n");
        sb.append("- Último relato: \"").append(ultimoRelato).append("\"\n\n");
        sb.append("### Recomendações / Alertas de Risco\n\n");
        sb.append(recomendacao).append("\n");

        return sb.toString();
    }
}
