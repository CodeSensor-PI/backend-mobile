-- V9: Dump Completo para Testes de Todos os Níveis de Acesso
-- Todos os usuários tem a senha: 123456

-- Limpando os dados (cuidado com chaves estrangeiras, melhor não truncar para não apagar o que já existe de importante, faremos INSERT IGNORE)

-- 1. Inserir Admin (Acesso Total)
INSERT INTO users (id, name, email, password, role) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Administrador Chefe', 'admin@email.com', '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', 'ADMIN')
ON CONFLICT (id) DO UPDATE SET password = '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', role = 'ADMIN';

-- 2. Inserir Psicólogo de Teste
INSERT INTO users (id, name, email, password, role) VALUES
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Psicólogo Teste', 'psicologo@email.com', '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', 'PSYCHOLOGIST')
ON CONFLICT (id) DO UPDATE SET password = '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', role = 'PSYCHOLOGIST';

-- 3. Inserir Paciente (Usuário comum)
INSERT INTO users (id, name, email, password, role) VALUES
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Paciente Teste', 'paciente@email.com', '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', 'CLIENTE')
ON CONFLICT (id) DO UPDATE SET password = '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa', role = 'CLIENTE';

-- 4. Registrar o paciente na tabela patients (ligado ao user ccc...)
INSERT INTO patients (id, name, email, phone, birth_date, user_id, cpf) VALUES
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Paciente Teste', 'paciente@email.com', '11988887777', '1990-01-01', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '12345678900')
ON CONFLICT DO NOTHING;

-- 5. Criar Agendamentos Mockados para os dashboards funcionarem
-- Sessões entre o Paciente e o Psicólogo

-- Sessão Concluída no passado
INSERT INTO sessoes (id, patient_id, psychologist_id, start_time, end_time, status, clinical_notes) VALUES
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '2 days' + time '10:00:00', CURRENT_DATE - INTERVAL '2 days' + time '11:00:00', 'CONCLUIDA', 'Paciente relatou melhora na ansiedade.')
ON CONFLICT DO NOTHING;

-- Sessão Agendada para o futuro (Semana atual)
INSERT INTO sessoes (id, patient_id, psychologist_id, start_time, end_time, status, clinical_notes) VALUES
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE + INTERVAL '2 days' + time '14:00:00', CURRENT_DATE + INTERVAL '2 days' + time '15:00:00', 'AGENDADA', '')
ON CONFLICT DO NOTHING;

-- Sessão Cancelada
INSERT INTO sessoes (id, patient_id, psychologist_id, start_time, end_time, status, clinical_notes) VALUES
('11111111-2222-3333-4444-555555555555', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', CURRENT_DATE - INTERVAL '5 days' + time '09:00:00', CURRENT_DATE - INTERVAL '5 days' + time '10:00:00', 'CANCELADA', 'Paciente não compareceu (No Show).')
ON CONFLICT DO NOTHING;
