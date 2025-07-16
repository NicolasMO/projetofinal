CREATE TABLE alocacao (
    id SERIAL PRIMARY KEY,
    numero_serie VARCHAR(70) NOT NULL REFERENCES equipamentos(numero_serie) ON DELETE RESTRICT,
    colaborador_id INTEGER NOT NULL REFERENCES colaborador(id) ON DELETE RESTRICT,
    data_envio DATE,
    data_entrega DATE,
    data_entrega_prevista DATE NOT NULL,
    data_devolucao DATE,
    data_devolucao_prevista DATE NOT NULL
);