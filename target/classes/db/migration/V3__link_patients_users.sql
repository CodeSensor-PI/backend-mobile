-- V3: Link patients to users and add crp to users

ALTER TABLE patients ADD COLUMN user_id UUID;
ALTER TABLE patients ADD CONSTRAINT fk_patients_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL;

-- Adicionando CRP na tabela de usuários para psicólogos
ALTER TABLE users ADD COLUMN crp VARCHAR(50);
ALTER TABLE users ADD COLUMN telefone VARCHAR(20);
