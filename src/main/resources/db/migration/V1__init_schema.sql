-- V1: Criação do esquema inicial do banco de dados

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    role VARCHAR(50)
);

CREATE TABLE patients (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255) NOT NULL,
    birth_date DATE,
    clinical_notes TEXT
);

CREATE TABLE feedbacks (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    content TEXT NOT NULL,
    mood_score INTEGER,
    CONSTRAINT fk_feedbacks_patients FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE reports (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    generated_at TIMESTAMP NOT NULL,
    ai_analysis_content TEXT NOT NULL,
    CONSTRAINT fk_reports_patients FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);
