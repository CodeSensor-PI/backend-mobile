package com.psi.rizerio.backend.feedback.infrastructure;

import com.psi.rizerio.backend.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByPatientIdOrderByCreatedAtDesc(UUID patientId);

    boolean existsByPatientIdAndSessaoId(UUID patientId, UUID sessaoId);

    Optional<Feedback> findByPatientIdAndSessaoId(UUID patientId, UUID sessaoId);

    List<Feedback> findBySessaoId(UUID sessaoId);
}
