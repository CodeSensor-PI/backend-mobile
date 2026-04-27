-- V4: Add missing fields to patients table
ALTER TABLE patients ADD COLUMN cpf VARCHAR(20) UNIQUE;
ALTER TABLE patients ADD COLUMN address VARCHAR(255);
ALTER TABLE patients ADD COLUMN neighborhood VARCHAR(255);
ALTER TABLE patients ADD COLUMN city VARCHAR(255);
ALTER TABLE patients ADD COLUMN state VARCHAR(100);
ALTER TABLE patients ADD COLUMN cep VARCHAR(20);
ALTER TABLE patients ADD COLUMN emergency_contact VARCHAR(255);
ALTER TABLE patients ADD COLUMN emergency_phone VARCHAR(20);
