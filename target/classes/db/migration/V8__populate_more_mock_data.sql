-- Adicionar mais clientes (Pacientes) com UUIDs reais e melhores para testar a Dashboard
INSERT INTO patients (id, name, email, phone, birth_date, clinical_notes, user_id, cpf, latitude, longitude) VALUES
(gen_random_uuid(), 'Marina Silva', 'marina.silva@email.com', '11988887771', '1990-05-20', 'Transtorno de Ansiedade Generalizada', '11111111-1111-1111-1111-111111111111', '12312312312', -23.5600, -46.6400),
(gen_random_uuid(), 'Roberto Costa', 'roberto.costa@email.com', '11988887772', '1985-08-14', 'Depressão Maior', '11111111-1111-1111-1111-111111111111', '12312312313', -23.5700, -46.6500),
(gen_random_uuid(), 'Juliana Almeida', 'juliana.almeida@email.com', '11988887773', '1995-12-01', 'TDAH', '11111111-1111-1111-1111-111111111111', '12312312314', -23.5500, -46.6300),
(gen_random_uuid(), 'Felipe Santos', 'felipe.santos@email.com', '11988887774', '2000-02-10', 'Bipolaridade', '11111111-1111-1111-1111-111111111111', '12312312315', -23.5900, -46.6800),
(gen_random_uuid(), 'Camila Ferreira', 'camila.ferreira@email.com', '11988887775', '1988-07-25', 'Síndrome do Pânico', '11111111-1111-1111-1111-111111111111', '12312312316', -23.6000, -46.6900);

-- Inserir Sessões
DO $$
DECLARE
    marina_id UUID;
    roberto_id UUID;
    juliana_id UUID;
    felipe_id UUID;
    camila_id UUID;
BEGIN
    SELECT id INTO marina_id FROM patients WHERE email = 'marina.silva@email.com' LIMIT 1;
    SELECT id INTO roberto_id FROM patients WHERE email = 'roberto.costa@email.com' LIMIT 1;
    SELECT id INTO juliana_id FROM patients WHERE email = 'juliana.almeida@email.com' LIMIT 1;
    SELECT id INTO felipe_id FROM patients WHERE email = 'felipe.santos@email.com' LIMIT 1;
    SELECT id INTO camila_id FROM patients WHERE email = 'camila.ferreira@email.com' LIMIT 1;

    INSERT INTO sessoes (id, patient_id, psychologist_id, start_time, end_time, status, clinical_notes) VALUES
    (gen_random_uuid(), marina_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '1 hour', 'CONCLUIDA', 'Paciente bem'),
    (gen_random_uuid(), roberto_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '1 hour', 'CANCELADA', ''),
    (gen_random_uuid(), juliana_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '1 hour', 'CONCLUIDA', ''),
    (gen_random_uuid(), felipe_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '1 hour', 'NO_SHOW', ''),
    (gen_random_uuid(), camila_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '1 hour', 'CONCLUIDA', ''),
    (gen_random_uuid(), marina_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '1 hour', 'CONCLUIDA', ''),
    (gen_random_uuid(), roberto_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 hour', 'CONCLUIDA', ''),
    (gen_random_uuid(), juliana_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP + INTERVAL '1 hour', CURRENT_TIMESTAMP + INTERVAL '2 hours', 'AGENDADA', ''),
    (gen_random_uuid(), felipe_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP + INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '1 hour', 'AGENDADA', ''),
    (gen_random_uuid(), camila_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP + INTERVAL '2 days', CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '1 hour', 'AGENDADA', ''),
    (gen_random_uuid(), marina_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP + INTERVAL '1 month', CURRENT_TIMESTAMP + INTERVAL '1 month' + INTERVAL '1 hour', 'AGENDADA', ''),
    (gen_random_uuid(), juliana_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '1 month', CURRENT_TIMESTAMP - INTERVAL '1 month' + INTERVAL '1 hour', 'CONCLUIDA', ''),
    (gen_random_uuid(), felipe_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '2 months', CURRENT_TIMESTAMP - INTERVAL '2 months' + INTERVAL '1 hour', 'CANCELADA', ''),
    (gen_random_uuid(), camila_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '2 months', CURRENT_TIMESTAMP - INTERVAL '2 months' + INTERVAL '1 hour', 'CONCLUIDA', ''),
    (gen_random_uuid(), roberto_id, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP - INTERVAL '3 months', CURRENT_TIMESTAMP - INTERVAL '3 months' + INTERVAL '1 hour', 'CONCLUIDA', '');
END $$;
