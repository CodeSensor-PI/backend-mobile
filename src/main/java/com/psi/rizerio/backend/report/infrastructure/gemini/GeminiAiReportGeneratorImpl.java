package com.psi.rizerio.backend.report.infrastructure.gemini;

import com.psi.rizerio.backend.report.domain.AiReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiAiReportGeneratorImpl implements AiReportGenerator {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Override
    public String generateReport(String prompt) {
        // Mocking the AI response because the Gemini API key is expired/invalid.
        // This avoids giant error logs in the console.
        System.out.println("Interceptado pedido para o Gemini (Chave expirada). Retornando Mock.");
        
        return "Resumo Gerado (Mock):\n\n" +
               "O paciente tem demonstrado uma progressão constante nas últimas sessões. " +
               "O humor principal relatado foi positivo, com níveis bons de motivação.\n\n" +
               "Recomendações:\n" +
               "- Explorar mais os tópicos de clareza trazidos no último formulário.\n" +
               "- Continuar o acompanhamento com foco no reforço positivo.";
    }
}
