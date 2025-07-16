
-- Alterando as colunas de datas para not null
ALTER TABLE colaborador
ALTER COLUMN data_demissao SET NOT NULL;

ALTER TABLE colaborador
ALTER COLUMN data_admissao SET NOT NULL;