-- V5: Add missing fields from JPA entities (latitude/longitude in patients, sessao_id in feedbacks)
ALTER TABLE patients ADD COLUMN latitude DOUBLE PRECISION;
ALTER TABLE patients ADD COLUMN longitude DOUBLE PRECISION;
ALTER TABLE feedbacks ADD COLUMN sessao_id UUID;
