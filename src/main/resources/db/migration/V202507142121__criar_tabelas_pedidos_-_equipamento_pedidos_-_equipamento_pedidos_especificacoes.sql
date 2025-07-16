CREATE TABLE pedidos (
    id SERIAL PRIMARY KEY,
    data_solicitacao DATE NOT NULL,
    data_previsao_entrega DATE NOT NULL,
    data_entrega DATE,
    status VARCHAR(40) NOT NULL
);

CREATE TABLE equipamento_pedidos (
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    tipo_equipamento_id INTEGER NOT null,
    entregue BOOLEAN DEFAULT FALSE,
    
    CONSTRAINT fk_entrega FOREIGN KEY (pedido_id)
        REFERENCES pedidos (id) ON DELETE CASCADE,

    CONSTRAINT fk_tipo_equipamento FOREIGN KEY (tipo_equipamento_id)
        REFERENCES tipos_equipamentos (id) ON DELETE RESTRICT
);

CREATE TABLE equipamento_pedidos_especificacoes (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    valor VARCHAR(100) NOT NULL,
	equipamento_pedido_id INTEGER NOT NULL,
    

    CONSTRAINT fk_item_pedido FOREIGN KEY (equipamento_pedido_id)
        REFERENCES equipamento_pedidos(id)
        ON DELETE CASCADE
);
