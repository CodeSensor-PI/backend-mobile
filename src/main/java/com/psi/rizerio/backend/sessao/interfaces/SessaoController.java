package com.psi.rizerio.backend.sessao.interfaces;

import com.psi.rizerio.backend.auth.domain.User;
import com.psi.rizerio.backend.auth.domain.UserRepository;
import com.psi.rizerio.backend.patient.domain.Patient;
import com.psi.rizerio.backend.patient.infrastructure.PatientRepository;
import com.psi.rizerio.backend.sessao.domain.Sessao;
import com.psi.rizerio.backend.sessao.infrastructure.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoRepository sessaoRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Sessao>> getAllSessoes() {
        return ResponseEntity.ok(sessaoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sessao> getSessaoById(@PathVariable UUID id) {
        return sessaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pacientes/{patientId}")
    public ResponseEntity<List<Sessao>> getSessoesByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(sessaoRepository.findByPatientId(patientId));
    }

    @GetMapping("/status")
    public ResponseEntity<List<Sessao>> getSessoesByStatus(@RequestParam String status) {
        return ResponseEntity.ok(sessaoRepository.findAll().stream()
                .filter(sessao -> String.valueOf(sessao.getStatus()).equalsIgnoreCase(status))
                .toList());
    }

    @PostMapping
    public ResponseEntity<Sessao> createSessao(@RequestBody SessaoRequestDTO request) {
        Sessao sessao = buildSessao(new Sessao(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoRepository.save(sessao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sessao> updateSessao(@PathVariable UUID id, @RequestBody SessaoRequestDTO request) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(sessaoRepository.save(buildSessao(sessao, request)));
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Sessao> cancelSessao(@PathVariable UUID id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow();
        sessao.setStatus("CANCELADA");
        return ResponseEntity.ok(sessaoRepository.save(sessao));
    }

    @GetMapping("/semana")
    public ResponseEntity<List<Sessao>> getSessoesSemana(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate segunda,
            @RequestParam(required = false, defaultValue = "40") int size) {
        
        LocalDateTime start = segunda.atStartOfDay();
        LocalDateTime end = segunda.plusDays(7).atTime(LocalTime.MAX);
        
        return ResponseEntity.ok(sessaoRepository.findByStartTimeBetween(start, end));
    }

    private Sessao buildSessao(Sessao sessao, SessaoRequestDTO request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        User psychologist = userRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));

        sessao.setPatient(patient);
        sessao.setPsychologist(psychologist);
        sessao.setStartTime(request.getStartTime());
        sessao.setEndTime(request.getEndTime());
        sessao.setStatus(request.getStatus() != null ? request.getStatus() : "AGENDADA");
        sessao.setClinicalNotes(request.getClinicalNotes());
        return sessao;
    }
}
