package com.psi.rizerio.backend.feedback.interfaces;

import com.psi.rizerio.backend.feedback.application.FeedbackService;
import com.psi.rizerio.backend.feedback.application.dto.FeedbackRequestDTO;
import com.psi.rizerio.backend.feedback.application.dto.FeedbackResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createFeedback(@Valid @RequestBody FeedbackRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.createFeedback(request));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbacksByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByPatient(patientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(@PathVariable UUID id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping("/session/{sessaoId}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackBySessao(@PathVariable UUID sessaoId) {
        return ResponseEntity.ok(feedbackService.getFeedbackBySessao(sessaoId));
    }
}
