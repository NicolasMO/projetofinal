CREATE TABLE parametros (
    id SERIAL PRIMARY KEY,
    tempo_medio_reposicao_dias INTEGER NOT NULL,
    tempo_medio_consumo_estoque_dias INTEGER NOT NULL,
    taxa_media_defeituosos_percentual DOUBLE PRECISION NOT NULL,
    tipo_equipamento_id BIGINT NOT NULL,
    CONSTRAINT fk_parametro_tipo_equipamento
        FOREIGN KEY (tipo_equipamento_id)
        REFERENCES tipos_equipamentos(id)
);

CREATE TABLE tempos_envio_por_regiao (
    id SERIAL PRIMARY KEY,
    regiao VARCHAR(30) NOT NULL,
    tempo_envio_dias INTEGER NOT NULL,
    parametro_id INTEGER NOT NULL REFERENCES parametros(id) ON DELETE CASCADE
);
