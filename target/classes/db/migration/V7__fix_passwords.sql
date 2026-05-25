-- V7: Fix passwords to use BCrypt and insert test patient because V6 already ran

UPDATE users SET password = '$2a$10$V8fq82l9Zpk7E.fnBFta5uIj4OQr2LguSsFthgPVWu8xmZjHQTBOa' WHERE password = '123456';

INSERT INTO patients (id, name, email, phone, birth_date, clinical_notes, photo, user_id, cpf, latitude, longitude) VALUES
('44444444-4444-4444-4444-444444444446', 'Novo Paciente', 'novo.paciente@email.com', '11999999996', NULL, NULL, NULL, '11111111-1111-1111-1111-111111111111', NULL, -23.5600, -46.6600)
ON CONFLICT DO NOTHING;
