package com.psi.rizerio.backend.config;

import com.psi.rizerio.backend.auth.domain.Role;
import com.psi.rizerio.backend.auth.domain.User;
import com.psi.rizerio.backend.auth.domain.UserRepository;
import com.psi.rizerio.backend.patient.domain.Patient;
import com.psi.rizerio.backend.patient.infrastructure.PatientRepository;
import com.psi.rizerio.backend.report.domain.Report;
import com.psi.rizerio.backend.report.infrastructure.ReportRepository;
import com.psi.rizerio.backend.sessao.domain.Sessao;
import com.psi.rizerio.backend.sessao.infrastructure.SessaoRepository;
import com.psi.rizerio.backend.feedback.domain.Feedback;
import com.psi.rizerio.backend.feedback.infrastructure.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final SessaoRepository sessaoRepository;
    private final ReportRepository reportRepository;
    private final FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedPatients();
        seedSessoesAndReports();
        seedFeedbacks();
    }

    private void seedUsers() {
        if (userRepository.findByEmail("admin@teste.com").isEmpty()) {
            User admin = User.builder()
                    .name("Administrador")
                    .email("admin@teste.com")
                    .password(passwordEncoder.encode("senha123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user created: admin@teste.com / senha123");
        }

        if (userRepository.findByEmail("psicologo@teste.com").isEmpty()) {
            User psicologo = User.builder()
                    .name("Psicólogo Teste")
                    .email("psicologo@teste.com")
                    .password(passwordEncoder.encode("senha123"))
                    .role(Role.PSYCHOLOGIST)
                    .crp("CRP 06/123456")
                    .telefone("11999998888")
                    .build();
            userRepository.save(psicologo);
            System.out.println("Psychologist user created: psicologo@teste.com / senha123");
        }

        if (userRepository.findByEmail("ana@teste.com").isEmpty()) {
            User ana = User.builder()
                    .name("Ana Psicóloga")
                    .email("ana@teste.com")
                    .password(passwordEncoder.encode("senha123"))
                    .role(Role.PSYCHOLOGIST)
                    .crp("CRP 06/654321")
                    .telefone("11988887777")
                    .build();
            userRepository.save(ana);
            System.out.println("Psychologist user created: ana@teste.com / senha123");
        }

        if (userRepository.findByEmail("roberto@teste.com").isEmpty()) {
            User roberto = User.builder()
                    .name("Roberto Psicólogo")
                    .email("roberto@teste.com")
                    .password(passwordEncoder.encode("senha123"))
                    .role(Role.PSYCHOLOGIST)
                    .crp("CRP 06/987654")
                    .telefone("11977776666")
                    .build();
            userRepository.save(roberto);
            System.out.println("Psychologist user created: roberto@teste.com / senha123");
        }

        if (userRepository.findByEmail("juliana@teste.com").isEmpty()) {
            User juliana = User.builder()
                    .name("Juliana Psicóloga")
                    .email("juliana@teste.com")
                    .password(passwordEncoder.encode("senha123"))
                    .role(Role.PSYCHOLOGIST)
                    .crp("CRP 06/456789")
                    .telefone("11966665555")
                    .build();
            userRepository.save(juliana);
            System.out.println("Psychologist user created: juliana@teste.com / senha123");
        }
        
        createPatientUser("paciente@teste.com", "Paciente Teste");
        createPatientUser("maria@teste.com", "Maria Oliveira");
        createPatientUser("carlos@teste.com", "Carlos Silva");
    }

    private void createPatientUser(String email, String name) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User patientUser = User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode("senha123"))
                    .role(Role.USER)
                    .build();
            userRepository.save(patientUser);
            System.out.println("Patient user created: " + email + " / senha123");
        }
    }

    private void seedPatients() {
        if (patientRepository.findByEmail("maria@teste.com").isEmpty()) {
            if (patientRepository.findByEmail("paciente@teste.com").isEmpty()) {
                createPatientProfile("paciente@teste.com", "João Paciente Teste", "11999999999", "12345678901", LocalDate.of(1990, 5, 15), "Paciente apresenta sintomas leves de ansiedade generalizada.", -23.550520, -46.633308);
            }
            createPatientProfile("maria@teste.com", "Maria Oliveira", "11988888888", "98765432100", LocalDate.of(1985, 8, 22), "Paciente busca acompanhamento para transtorno depressivo maior em remissão.", -23.559616, -46.658027);
            createPatientProfile("carlos@teste.com", "Carlos Silva", "11977777777", "45612378900", LocalDate.of(2000, 2, 10), "Paciente universitário lidando com TDAH e dificuldades acadêmicas.", -23.567849, -46.648908);
        }
    }

    private void createPatientProfile(String email, String fullName, String phone, String cpf, LocalDate birthDate, String clinicalNotes, Double lat, Double lng) {
        User patientUser = userRepository.findByEmail(email).orElseThrow();
        Patient patient = Patient.builder()
                .name(fullName)
                .email(email)
                .phone(phone)
                .cpf(cpf)
                .birthDate(birthDate)
                .clinicalNotes(clinicalNotes)
                .userId(patientUser.getId())
                .address("Rua Teste, 123")
                .city("São Paulo")
                .state("SP")
                .latitude(lat)
                .longitude(lng)
                .build();
        patientRepository.save(patient);
        System.out.println("Test patient created: " + patient.getName());
    }

    private void seedSessoesAndReports() {
        if (sessaoRepository.count() < 3) {
            User psicologo = userRepository.findByEmail("psicologo@teste.com").orElseThrow();
            
            Patient joao = patientRepository.findByEmail("paciente@teste.com").orElse(null);
            Patient maria = patientRepository.findByEmail("maria@teste.com").orElse(null);
            Patient carlos = patientRepository.findByEmail("carlos@teste.com").orElse(null);

            if (joao != null) {
                createSessao(joao, psicologo, LocalDateTime.now().minusDays(14).withHour(9).withMinute(0), "CONCLUIDA", "Alinhamento de expectativas e anamnese inicial.");
                createSessao(joao, psicologo, LocalDateTime.now().minusDays(7).withHour(9).withMinute(0), "CONCLUIDA", "Sessão inicial. Paciente relatou ansiedade no trabalho.");
                createSessao(joao, psicologo, LocalDateTime.now().minusDays(2).withHour(9).withMinute(0), "CONCLUIDA", "Trabalhamos técnicas de relaxamento e regulação de respiração.");
                createSessao(joao, psicologo, LocalDateTime.now().plusDays(2).withHour(9).withMinute(0), "AGENDADA", "");
            }

            if (maria != null) {
                createSessao(maria, psicologo, LocalDateTime.now().minusDays(15).withHour(14).withMinute(0), "CONCLUIDA", "Revisão de rotina e higiene do sono.");
                createSessao(maria, psicologo, LocalDateTime.now().minusDays(8).withHour(14).withMinute(0), "CANCELADA", "Paciente cancelou por motivos de saúde.");
                createSessao(maria, psicologo, LocalDateTime.now().minusDays(1).withHour(14).withMinute(0), "CANCELADA", "Paciente cancelou de última hora devido a imprevisto pessoal.");
                createSessao(maria, psicologo, LocalDateTime.now().plusDays(6).withHour(14).withMinute(0), "AGENDADA", "");
            }

            if (carlos != null) {
                createSessao(carlos, psicologo, LocalDateTime.now().minusDays(3).withHour(16).withMinute(0), "CONCLUIDA", "Sessão sobre técnicas de foco, TDAH e produtividade acadêmica.");
                createSessao(carlos, psicologo, LocalDateTime.now().plusDays(4).withHour(16).withMinute(0), "AGENDADA", "");
            }
        }
    }

    private void seedFeedbacks() {
        if (feedbackRepository.count() == 0) {
            Patient joao = patientRepository.findByEmail("paciente@teste.com").orElse(null);
            Patient carlos = patientRepository.findByEmail("carlos@teste.com").orElse(null);
            Patient maria = patientRepository.findByEmail("maria@teste.com").orElse(null);

            if (joao != null) {
                createFeedback(joao, "Me senti muito ansioso na segunda-feira por conta de uma entrega no trabalho, mas consegui usar a técnica de respiração sugerida.", 3, LocalDateTime.now().minusDays(6));
                createFeedback(joao, "Consegui me posicionar melhor na reunião hoje. Senti uma sensação de alívio e controle.", 4, LocalDateTime.now().minusDays(1));
            }

            if (carlos != null) {
                createFeedback(carlos, "Lidando com muitas distrações para estudar para as provas finais. Tentei a técnica Pomodoro mas foi difícil manter a constância.", 2, LocalDateTime.now().minusDays(2));
            }
            
            if (maria != null) {
                createFeedback(maria, "Sentimento de desmotivação muito forte essa semana, com dificuldades para levantar da cama.", 2, LocalDateTime.now().minusDays(14));
            }
        }
    }

    private void createFeedback(Patient patient, String content, int moodScore, LocalDateTime createdAt) {
        Feedback f = Feedback.builder()
                .patient(patient)
                .content(content)
                .moodScore(moodScore)
                .createdAt(createdAt)
                .build();
        feedbackRepository.save(f);
    }

    private void createSessao(Patient patient, User psicologo, LocalDateTime startTime, String status, String clinicalNotes) {
        Sessao sessao = Sessao.builder()
                .patient(patient)
                .psychologist(psicologo)
                .startTime(startTime)
                .endTime(startTime.plusMinutes(50))
                .status(status)
                .clinicalNotes(clinicalNotes)
                .build();
        sessaoRepository.save(sessao);
    }
}
