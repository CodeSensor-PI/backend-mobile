-- Localização do feedback (capturada no app no momento do envio)
ALTER TABLE feedbacks ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION;
ALTER TABLE feedbacks ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION;
ALTER TABLE feedbacks ADD COLUMN IF NOT EXISTS location_label VARCHAR(255);

-- Feedback geral (sem sessão) passa a ser permitido
ALTER TABLE feedbacks ALTER COLUMN sessao_id DROP NOT NULL;
