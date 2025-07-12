CREATE TABLE tipos_equipamentos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(60) NOT null UNIQUE,
    tempo_configuracao_dias INTEGER
);

CREATE TABLE especificacoes (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(60),
    valor VARCHAR(60),
    CONSTRAINT uq_descricao_valor UNIQUE (descricao, valor)
);

CREATE TABLE equipamentos (
    numero_serie VARCHAR(70) PRIMARY key UNIQUE,
    modelo VARCHAR(50) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    data_aquisicao DATE,
    tempo_uso INTEGER,
    status varchar(40) NOT NULL,
    tipo_equipamento_id INTEGER,
    CONSTRAINT fk_tipo_equipamento
        FOREIGN KEY(tipo_equipamento_id) REFERENCES tipos_equipamentos(id)
        ON DELETE RESTRICT
);

CREATE TABLE equipamentos_especificacoes (
    numero_serie VARCHAR(70),
    especificacao_id INTEGER,
    PRIMARY KEY (numero_serie, especificacao_id),
    CONSTRAINT fk_equipamento
        FOREIGN KEY(numero_serie) REFERENCES equipamentos(numero_serie)
        ON DELETE CASCADE,
    CONSTRAINT fk_especificacao
        FOREIGN KEY(especificacao_id) REFERENCES especificacoes(id)
        ON DELETE RESTRICT
);