CREATE TABLE tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    data_expiracao TIMESTAMP NOT NULL,
    utilizado BOOLEAN NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
