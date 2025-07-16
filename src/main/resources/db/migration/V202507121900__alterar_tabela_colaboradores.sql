
-- Adicionar coluna status_ativo como boolean com default TRUE
ALTER TABLE colaborador
ADD COLUMN status_ativo BOOLEAN DEFAULT TRUE;