-- V2: Adição de campo foto e tabela de sessões

ALTER TABLE users ADD COLUMN photo TEXT;
ALTER TABLE patients ADD COLUMN photo TEXT;

CREATE TABLE sessoes (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    psychologist_id UUID NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    status VARCHAR(50),
    clinical_notes TEXT,
    CONSTRAINT fk_sessoes_patients FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    CONSTRAINT fk_sessoes_users FOREIGN KEY (psychologist_id) REFERENCES users(id) ON DELETE CASCADE
);
