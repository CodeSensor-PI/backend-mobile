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
import java.util.List;

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
        seedAgendfyData();
        seedFotosEDadosFaltantes();
    }

    // Preenche fotos (avatar público) e campos vazios de pacientes e psicólogos,
    // para nenhuma lista ficar com foto padrão ou dados em branco. Idempotente:
    // só altera o que estiver nulo/vazio.
    private void seedFotosEDadosFaltantes() {
        int p = 0;
        for (Patient pac : patientRepository.findAll()) {
            boolean alterado = false;
            if (isBlank(pac.getPhoto())) {
                pac.setPhoto("https://i.pravatar.cc/200?u=" + encodeKey(pac.getEmail() != null ? pac.getEmail() : pac.getId().toString()));
                alterado = true;
            }
            if (isBlank(pac.getPhone())) { pac.setPhone(String.format("119%08d", 10000000 + (p % 89999999))); alterado = true; }
            if (isBlank(pac.getEmergencyContact())) { pac.setEmergencyContact("Contato de Emergência"); alterado = true; }
            if (isBlank(pac.getEmergencyPhone())) { pac.setEmergencyPhone(String.format("119%08d", 20000000 + (p % 79999999))); alterado = true; }
            if (isBlank(pac.getCity())) { pac.setCity("São Paulo"); alterado = true; }
            if (isBlank(pac.getState())) { pac.setState("SP"); alterado = true; }
            if (isBlank(pac.getNeighborhood())) { pac.setNeighborhood("Centro"); alterado = true; }
            if (isBlank(pac.getClinicalNotes())) { pac.setClinicalNotes("Acompanhamento psicológico em andamento."); alterado = true; }
            if (alterado) patientRepository.save(pac);
            p++;
        }

        int u = 0;
        for (User user : userRepository.findAll()) {
            if (user.getRole() != Role.PSYCHOLOGIST) {
                continue;
            }
            boolean alterado = false;
            if (isBlank(user.getPhoto())) {
                user.setPhoto("https://i.pravatar.cc/200?u=" + encodeKey(user.getEmail() != null ? user.getEmail() : user.getId().toString()));
                alterado = true;
            }
            if (isBlank(user.getTelefone())) { user.setTelefone(String.format("119%08d", 30000000 + (u % 69999999))); alterado = true; }
            if (isBlank(user.getCrp())) { user.setCrp(String.format("CRP 06/%06d", 100000 + (u % 899999))); alterado = true; }
            if (alterado) userRepository.save(user);
            u++;
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String encodeKey(String key) {
        return key.replaceAll("[^a-zA-Z0-9]", "");
    }

    // Dados ricos para popular a aplicação: pacientes do app real (agendfy),
    // com perfis, sessões (passadas e futuras) e feedbacks com localização.
    private void seedAgendfyData() {
        User marcos = userRepository.findByEmail("marcos.psico@agendfy.com").orElse(null);
        User beatriz = userRepository.findByEmail("beatriz.psico@agendfy.com").orElse(null);
        User rafael = userRepository.findByEmail("rafael.psico@agendfy.com").orElse(null);
        if (marcos == null) {
            return;
        }

        seedPatientProfileIfMissing("ana.cliente@agendfy.com", "Ana Souza", "11991110001", "10100100101", LocalDate.of(1992, 3, 12), "Ansiedade relacionada a desempenho profissional.", -23.561414, -46.655881);
        seedPatientProfileIfMissing("carlos.cliente@agendfy.com", "Carlos Lima", "11991110002", "10100100102", LocalDate.of(1988, 11, 5), "Acompanhamento de luto.", -23.588, -46.632);
        seedPatientProfileIfMissing("mariana.cliente@agendfy.com", "Mariana Rocha", "11991110003", "10100100103", LocalDate.of(1995, 7, 22), "Estresse e dificuldades de sono.", -23.5489, -46.6388);
        seedPatientProfileIfMissing("joao.cliente@agendfy.com", "João Mendes", "11991110004", "10100100104", LocalDate.of(1990, 1, 30), "Manejo de raiva e relacionamentos.", -23.5733, -46.6417);
        seedPatientProfileIfMissing("fernanda.cliente@agendfy.com", "Fernanda Dias", "11991110005", "10100100105", LocalDate.of(1998, 9, 9), "Autoestima e imagem corporal.", -23.5955, -46.6840);
        seedPatientProfileIfMissing("bruno.cliente@agendfy.com", "Bruno Carvalho", "11991110006", "10100100106", LocalDate.of(1985, 5, 18), "Burnout e equilíbrio trabalho-vida.", -23.5311, -46.6253);

        // Garante agenda para cada paciente de teste (idempotente: só cria se o
        // paciente ainda não tem nenhuma sessão).
        String[] emails = {"ana.cliente@agendfy.com", "carlos.cliente@agendfy.com", "mariana.cliente@agendfy.com", "joao.cliente@agendfy.com", "fernanda.cliente@agendfy.com", "bruno.cliente@agendfy.com"};
        User[] psicologos = {marcos, beatriz != null ? beatriz : marcos, rafael != null ? rafael : marcos};
        int i = 0;
        for (String email : emails) {
            Patient p = patientRepository.findByEmail(email).orElse(null);
            if (p == null) {
                i++;
                continue;
            }
            if (sessaoRepository.findByPatientId(p.getId()).isEmpty()) {
                User psi = psicologos[i % psicologos.length];
                createSessao(p, psi, LocalDateTime.now().minusDays(20 - i).withHour(10).withMinute(0), "CONCLUIDA", "Anamnese inicial e definição de objetivos.");
                createSessao(p, psi, LocalDateTime.now().minusDays(10 - i).withHour(10).withMinute(0), "CONCLUIDA", "Aplicação de técnicas e revisão de tarefas.");
                createSessao(p, psi, LocalDateTime.now().minusDays(3).withHour(11).withMinute(0), "CANCELADA", "Cancelada pelo paciente.");
                createSessao(p, psi, LocalDateTime.now().plusDays(3 + i).withHour(15).withMinute(0), "AGENDADA", "");
                createSessao(p, psi, LocalDateTime.now().plusDays(12 + i).withHour(15).withMinute(0), "AGENDADA", "");
            }
            i++;
        }

        if (feedbackRepository.count() < 8) {
            createFeedbackWithLocation("ana.cliente@agendfy.com", "Semana mais tranquila, consegui aplicar a respiração antes da apresentação.", 4, LocalDateTime.now().minusDays(5), -23.561414, -46.655881, "São Paulo, SP");
            createFeedbackWithLocation("carlos.cliente@agendfy.com", "Dias difíceis, mas escrevi no diário como combinamos.", 2, LocalDateTime.now().minusDays(3), -23.588, -46.632, "São Paulo, SP");
            createFeedbackWithLocation("mariana.cliente@agendfy.com", "Dormi melhor depois de cortar a cafeína à noite.", 4, LocalDateTime.now().minusDays(2), -23.5489, -46.6388, "São Paulo, SP");
            createFeedbackWithLocation("joao.cliente@agendfy.com", "Tive um episódio de raiva no trânsito, mas percebi o gatilho.", 3, LocalDateTime.now().minusDays(1), -23.5733, -46.6417, "São Paulo, SP");
        }

        seedDadosRicosAna();
    }

    // Conta de demonstração: muitas sessões (passadas e futuras) e feedbacks
    // ricos para Ana, ideal para testar feedback por sessão e relatórios de IA.
    private void seedDadosRicosAna() {
        Patient ana = patientRepository.findByEmail("ana.cliente@agendfy.com").orElse(null);
        // Ana é vinculada ao psicólogo teste@email.com (conta de demonstração).
        User psi = userRepository.findByEmail("teste@email.com")
                .orElseGet(() -> userRepository.findByEmail("marcos.psico@agendfy.com").orElse(null));
        if (ana == null || psi == null) {
            return;
        }

        // Garante o vínculo: reatribui todas as sessões existentes de Ana ao psi.
        List<Sessao> sessoesAna = sessaoRepository.findByPatientId(ana.getId());
        for (Sessao s : sessoesAna) {
            if (s.getPsychologist() == null || !psi.getId().equals(s.getPsychologist().getId())) {
                s.setPsychologist(psi);
                sessaoRepository.save(s);
            }
        }

        // Sessões extras (idempotente: só adiciona se Ana tiver poucas sessões).
        if (sessoesAna.size() < 10) {
            createSessao(ana, psi, LocalDateTime.now().minusDays(60).withHour(9).withMinute(0), "CONCLUIDA", "Anamnese e definição de objetivos terapêuticos.");
            createSessao(ana, psi, LocalDateTime.now().minusDays(45).withHour(9).withMinute(0), "CONCLUIDA", "Psicoeducação sobre ansiedade e respiração diafragmática.");
            createSessao(ana, psi, LocalDateTime.now().minusDays(38).withHour(9).withMinute(0), "CANCELADA", "Cancelada por imprevisto de trabalho.");
            createSessao(ana, psi, LocalDateTime.now().minusDays(31).withHour(9).withMinute(0), "CONCLUIDA", "Reestruturação cognitiva de pensamentos catastróficos.");
            createSessao(ana, psi, LocalDateTime.now().minusDays(24).withHour(9).withMinute(0), "CONCLUIDA", "Treino de exposição gradual a situações de apresentação.");
            createSessao(ana, psi, LocalDateTime.now().minusDays(17).withHour(9).withMinute(0), "CONCLUIDA", "Revisão de tarefas e manejo de gatilhos no trabalho.");
            createSessao(ana, psi, LocalDateTime.now().minusDays(4).withHour(9).withMinute(0), "CONCLUIDA", "Consolidação de estratégias e prevenção de recaída.");
            createSessao(ana, psi, LocalDateTime.now().plusDays(2).withHour(9).withMinute(0), "AGENDADA", "");
            createSessao(ana, psi, LocalDateTime.now().plusDays(9).withHour(9).withMinute(0), "AGENDADA", "");
            createSessao(ana, psi, LocalDateTime.now().plusDays(16).withHour(9).withMinute(0), "AGENDADA", "");
        }

        // Feedbacks ricos para alimentar os relatórios de IA.
        long feedbacksAna = feedbackRepository.findByPatientIdOrderByCreatedAtDesc(ana.getId()).size();
        if (feedbacksAna < 5) {
            createFeedbackWithLocation("ana.cliente@agendfy.com", "Comecei o tratamento bastante ansiosa, com dificuldade de dormir antes de reuniões importantes.", 2, LocalDateTime.now().minusDays(44), -23.561414, -46.655881, "São Paulo, SP");
            createFeedbackWithLocation("ana.cliente@agendfy.com", "A técnica de respiração ajudou um pouco, mas ainda travo em apresentações.", 3, LocalDateTime.now().minusDays(30), -23.561414, -46.655881, "São Paulo, SP");
            createFeedbackWithLocation("ana.cliente@agendfy.com", "Consegui fazer uma apresentação sem pânico pela primeira vez. Me senti no controle.", 4, LocalDateTime.now().minusDays(16), -23.561414, -46.655881, "São Paulo, SP");
            createFeedbackWithLocation("ana.cliente@agendfy.com", "Semana puxada, mas usei as estratégias e dormi melhor. Humor estável.", 4, LocalDateTime.now().minusDays(3), -23.561414, -46.655881, "São Paulo, SP");
        }
    }

    private void seedPatientProfileIfMissing(String email, String fullName, String phone, String cpf, LocalDate birthDate, String clinicalNotes, Double lat, Double lng) {
        if (patientRepository.findByEmail(email).isPresent()) {
            return;
        }
        createPatientProfile(email, fullName, phone, cpf, birthDate, clinicalNotes, lat, lng);
    }

    private void createFeedbackWithLocation(String email, String content, int moodScore, LocalDateTime createdAt, Double lat, Double lng, String label) {
        Patient patient = patientRepository.findByEmail(email).orElse(null);
        if (patient == null) {
            return;
        }
        Feedback f = Feedback.builder()
                .patient(patient)
                .content(content)
                .moodScore(moodScore)
                .createdAt(createdAt)
                .latitude(lat)
                .longitude(lng)
                .locationLabel(label)
                .build();
        feedbackRepository.save(f);
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

        // Usuários documentados no app (TEST-USERS.md) — para que as credenciais
        // usadas no mobile funcionem direto contra o backend real (sem mock).
        createPsychologist("teste@email.com", "Psicólogo", "123456", "CRP 06/111111", "11999990000");
        createPsychologist("marcos.psico@agendfy.com", "Marcos Psicólogo", "Psico@1234", "CRP 06/222222", "11999990001");
        createPsychologist("beatriz.psico@agendfy.com", "Beatriz Psicóloga", "123456", "CRP 06/333333", "11999990002");
        createPsychologist("rafael.psico@agendfy.com", "Rafael Psicólogo", "123456", "CRP 06/444444", "11999990003");
        createPsychologist("juliana.psico@agendfy.com", "Juliana Psicóloga", "123456", "CRP 06/555555", "11999990004");
        createAdmin("admin@agendfy.com", "Administrador Agendfy", "Admin@1234");

        createPatientUserWithPassword("ana.cliente@agendfy.com", "Ana Souza", "Cliente@1234");
        createPatientUserWithPassword("carlos.cliente@agendfy.com", "Carlos Lima", "Cliente@1234");
        createPatientUserWithPassword("mariana.cliente@agendfy.com", "Mariana Rocha", "123456");
        createPatientUserWithPassword("joao.cliente@agendfy.com", "João Mendes", "123456");
        createPatientUserWithPassword("fernanda.cliente@agendfy.com", "Fernanda Dias", "123456");
        createPatientUserWithPassword("bruno.cliente@agendfy.com", "Bruno Carvalho", "123456");
        // Paciente de primeiro acesso (sem perfil/CPF) — abre o formulário inicial.
        createPatientUserWithPassword("novo.paciente@email.com", "Paciente Novo", "123456");
    }

    private void createPsychologist(String email, String name, String password, String crp, String telefone) {
        if (userRepository.findByEmail(email).isEmpty()) {
            userRepository.save(User.builder()
                    .name(name).email(email)
                    .password(passwordEncoder.encode(password))
                    .role(Role.PSYCHOLOGIST).crp(crp).telefone(telefone)
                    .build());
            System.out.println("Psychologist user created: " + email + " / " + password);
        }
    }

    private void createAdmin(String email, String name, String password) {
        if (userRepository.findByEmail(email).isEmpty()) {
            userRepository.save(User.builder()
                    .name(name).email(email)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .build());
            System.out.println("Admin user created: " + email + " / " + password);
        }
    }

    private void createPatientUserWithPassword(String email, String name, String password) {
        if (userRepository.findByEmail(email).isEmpty()) {
            userRepository.save(User.builder()
                    .name(name).email(email)
                    .password(passwordEncoder.encode(password))
                    .role(Role.USER)
                    .build());
            System.out.println("Patient user created: " + email + " / " + password);
        }
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
