package com.psi.rizerio.backend.sessao.infrastructure;

import com.psi.rizerio.backend.sessao.domain.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
    List<Sessao> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Sessao> findByPatientId(UUID patientId);
}
