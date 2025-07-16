CREATE TABLE alocacao_equipamento (
    id SERIAL PRIMARY KEY,
    numero_serie VARCHAR(70) NOT NULL REFERENCES equipamentos(numero_serie) ON DELETE RESTRICT,
    colaborador_id INTEGER NOT NULL REFERENCES colaborador(id) ON DELETE RESTRICT,
    
    data_entrega_prevista DATE NOT NULL,
    data_entrega DATE,
    
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao DATE,
    
    devolvido BOOLEAN NOT NULL DEFAULT false,
    
    status_equipamento VARCHAR(40) NOT NULL,
    
    observacao TEXT,
    
    CONSTRAINT chk_data_entrega CHECK (
        data_entrega IS NULL OR data_entrega >= data_entrega_prevista
    ),
    CONSTRAINT chk_data_devolucao CHECK (
        data_devolucao IS NULL OR data_devolucao >= data_entrega
    )
);