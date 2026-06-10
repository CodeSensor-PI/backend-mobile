package com.psi.rizerio.backend.report.infrastructure.gemini;

import com.psi.rizerio.backend.report.domain.AiReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiAiReportGeneratorImpl implements AiReportGenerator {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.base-url:https://generativelanguage.googleapis.com/v1beta/models}")
    private String baseUrl;

    // Lista de modelos em ordem de preferência. Se um falhar (cota/erro/503),
    // tenta o próximo, garantindo que a IA quase nunca fique indisponível.
    @Value("${gemini.api.models:gemini-2.5-flash,gemini-2.5-flash-lite,gemini-3.1-flash-lite}")
    private String models;

    private final RestTemplate restTemplate;

    @Override
    public String generateReport(String prompt) {
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("dummy")) {
            return "Erro ao gerar relatório: chave da API Gemini não configurada.";
        }

        GeminiRequest.Part part = new GeminiRequest.Part();
        part.setText(prompt);
        GeminiRequest.Content content = new GeminiRequest.Content();
        content.setParts(List.of(part));
        GeminiRequest request = new GeminiRequest();
        request.setContents(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        List<String> modelList = Arrays.stream(models.split(","))
                .map(String::trim)
                .filter(m -> !m.isEmpty())
                .toList();

        String lastError = "nenhum modelo disponível";

        for (String model : modelList) {
            String url = baseUrl + "/" + model + ":generateContent?key=" + apiKey;
            try {
                // Retry para instabilidades transitórias (503 "high demand").
                GeminiResponse response = null;
                int maxTentativas = 3;
                for (int tentativa = 1; tentativa <= maxTentativas; tentativa++) {
                    try {
                        response = restTemplate.postForObject(url, entity, GeminiResponse.class);
                        break;
                    } catch (HttpServerErrorException e) {
                        lastError = e.getMessage();
                        if (tentativa == maxTentativas) {
                            throw e;
                        }
                        Thread.sleep(1200L * tentativa);
                    }
                }

                String text = extractText(response);
                if (text != null && !text.isBlank()) {
                    return text;
                }
                lastError = "resposta vazia do modelo " + model;
            } catch (Exception e) {
                // Cota (429), modelo indisponível, etc.: tenta o próximo modelo.
                lastError = e.getMessage();
                System.out.println("Modelo Gemini '" + model + "' falhou: " + e.getMessage());
            }
        }

        return "Erro ao gerar relatório: " + lastError;
    }

    private String extractText(GeminiResponse response) {
        if (response != null
                && response.getCandidates() != null
                && !response.getCandidates().isEmpty()) {
            GeminiResponse.Candidate candidate = response.getCandidates().get(0);
            if (candidate.getContent() != null
                    && candidate.getContent().getParts() != null
                    && !candidate.getContent().getParts().isEmpty()) {
                return candidate.getContent().getParts().get(0).getText();
            }
        }
        return null;
    }
}
