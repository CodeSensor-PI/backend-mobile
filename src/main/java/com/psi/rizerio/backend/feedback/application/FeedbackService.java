package com.psi.rizerio.backend.feedback.application;

import com.psi.rizerio.backend.feedback.application.dto.FeedbackRequestDTO;
import com.psi.rizerio.backend.feedback.application.dto.FeedbackResponseDTO;
import com.psi.rizerio.backend.feedback.application.mapper.FeedbackMapper;
import com.psi.rizerio.backend.feedback.domain.Feedback;
import com.psi.rizerio.backend.feedback.infrastructure.FeedbackRepository;
import com.psi.rizerio.backend.patient.domain.Patient;
import com.psi.rizerio.backend.patient.infrastructure.PatientRepository;
import com.psi.rizerio.backend.sessao.domain.Sessao;
import com.psi.rizerio.backend.sessao.infrastructure.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PatientRepository patientRepository;
    private final SessaoRepository sessaoRepository;
    private final FeedbackMapper mapper;

    public FeedbackResponseDTO createFeedback(FeedbackRequestDTO request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado."));

        // Feedback vinculado a uma sessão: valida pertencimento e status concluído.
        // Feedback geral (sessaoId nulo): aceito sem sessão, apenas com humor/localização.
        if (request.getSessaoId() != null) {
            Sessao sessao = sessaoRepository.findById(request.getSessaoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada."));

            boolean sessaoPertenceAoPaciente = sessao.getPatient() != null
                    && patient.getId().equals(sessao.getPatient().getId());
            if (!sessaoPertenceAoPaciente) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A sessão informada não pertence ao paciente.");
            }

            String statusSessao = String.valueOf(sessao.getStatus()).toUpperCase();
            if (!"CONCLUIDA".equals(statusSessao)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente sessões concluídas aceitam feedback.");
            }
        }

        Optional<Feedback> existingFeedback = request.getSessaoId() == null
                ? Optional.empty()
                : feedbackRepository.findByPatientIdAndSessaoId(request.getPatientId(), request.getSessaoId());

        Feedback feedback;
        if (existingFeedback.isPresent()) {
            feedback = existingFeedback.get();
            feedback.setContent(request.getContent());
            feedback.setMoodScore(request.getMoodScore());
            feedback.setLatitude(request.getLatitude());
            feedback.setLongitude(request.getLongitude());
            feedback.setLocationLabel(request.getLocationLabel());
        } else {
            feedback = mapper.toEntity(request);
            feedback.setPatient(patient);
            feedback.setSessaoId(request.getSessaoId());
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return mapper.toDto(savedFeedback);
    }

    public List<FeedbackResponseDTO> getFeedbacksByPatient(UUID patientId) {
        return feedbackRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public FeedbackResponseDTO getFeedbackById(UUID id) {
        return feedbackRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback não encontrado."));
    }

    public FeedbackResponseDTO getFeedbackBySessao(UUID sessaoId) {
        return feedbackRepository.findBySessaoId(sessaoId)
                .stream()
                .findFirst()
                .map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback para esta sessão não encontrado."));
    }
}
