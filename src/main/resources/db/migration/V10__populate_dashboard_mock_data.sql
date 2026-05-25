-- V10: Popular dashboard com dados ricos para apresentação

-- 1. Inserir Novos Pacientes e seus respectivos usuários
INSERT INTO users (id, name, email, password, role) VALUES
('22222222-2222-2222-2222-222222222222', 'Maria Silva', 'maria@email.com', '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', 'CLIENTE'),
('33333333-3333-3333-3333-333333333333', 'João Souza', 'joao@email.com', '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', 'CLIENTE')
ON CONFLICT DO NOTHING;

INSERT INTO patients (id, name, email, phone, birth_date, user_id, cpf) VALUES
('44444444-4444-4444-4444-444444444444', 'Maria Silva', 'maria@email.com', '11988887777', '1995-05-10', '22222222-2222-2222-2222-222222222222', '23456789011'),
('55555555-5555-5555-5555-555555555555', 'João Souza', 'joao@email.com', '11988887777', '1988-11-20', '33333333-3333-3333-3333-333333333333', '34567890122')
ON CONFLICT DO NOTHING;

-- 2. Sessões de Maria (Tendência constante nos últimos 6 meses, com saúde financeira e engajamento)
INSERT INTO sessoes (id, patient_id, psychologist_id, start_time, end_time, status, clinical_notes) VALUES
(gen_random_uuid(), '44444444-4444-4444-4444-444444444444', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '1 month' + time '10:00:00', CURRENT_DATE - INTERVAL '1 month' + time '11:00:00', 'CONCLUIDA', 'Paciente com queixas de ansiedade.'),
(gen_random_uuid(), '44444444-4444-4444-4444-444444444444', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '2 month' + time '10:00:00', CURRENT_DATE - INTERVAL '2 month' + time '11:00:00', 'CONCLUIDA', 'Melhora no quadro ansioso.'),
(gen_random_uuid(), '44444444-4444-4444-4444-444444444444', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '3 month' + time '10:00:00', CURRENT_DATE - INTERVAL '3 month' + time '11:00:00', 'CONCLUIDA', 'Sessão produtiva.'),
(gen_random_uuid(), '44444444-4444-4444-4444-444444444444', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '4 month' + time '10:00:00', CURRENT_DATE - INTERVAL '4 month' + time '11:00:00', 'CONCLUIDA', 'Relatos de insônia e ansiedade generalizada.'),
(gen_random_uuid(), '44444444-4444-4444-4444-444444444444', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '5 month' + time '10:00:00', CURRENT_DATE - INTERVAL '5 month' + time '11:00:00', 'CONCLUIDA', 'Início do acompanhamento.');

-- 3. Sessões de João (Muitos cancelamentos, gera dados para a área de No-show e risco de evasão)
INSERT INTO sessoes (id, patient_id, psychologist_id, start_time, end_time, status, clinical_notes) VALUES
(gen_random_uuid(), '55555555-5555-5555-5555-555555555555', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '7 days' + time '15:00:00', CURRENT_DATE - INTERVAL '7 days' + time '16:00:00', 'CANCELADA', 'Paciente não compareceu (No Show).'),
(gen_random_uuid(), '55555555-5555-5555-5555-555555555555', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '14 days' + time '15:00:00', CURRENT_DATE - INTERVAL '14 days' + time '16:00:00', 'CANCELADA', 'Desmarcou de última hora.'),
(gen_random_uuid(), '55555555-5555-5555-5555-555555555555', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '21 days' + time '15:00:00', CURRENT_DATE - INTERVAL '21 days' + time '16:00:00', 'CONCLUIDA', 'Sessão difícil, paciente desmotivado e relatando falta de foco no trabalho associado a TDAH.'),
(gen_random_uuid(), '55555555-5555-5555-5555-555555555555', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '1 month' + time '15:00:00', CURRENT_DATE - INTERVAL '1 month' + time '16:00:00', 'CONCLUIDA', 'Sessão regular, mas com indícios de depressão.');

-- 4. Sessões para hoje e para os próximos dias (Pacientes do Dia, Carga horária, Saúde financeira projetada)
INSERT INTO sessoes (id, patient_id, psychologist_id, start_time, end_time, status, clinical_notes) VALUES
(gen_random_uuid(), 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE + time '13:00:00', CURRENT_DATE + time '14:00:00', 'AGENDADA', ''),
(gen_random_uuid(), '44444444-4444-4444-4444-444444444444', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE + time '16:00:00', CURRENT_DATE + time '17:00:00', 'AGENDADA', ''),
(gen_random_uuid(), '55555555-5555-5555-5555-555555555555', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE + INTERVAL '1 day' + time '18:00:00', CURRENT_DATE + INTERVAL '1 day' + time '19:00:00', 'AGENDADA', '');
