CREATE TABLE colaborador (
id SERIAL PRIMARY KEY,
nome VARCHAR(255) NOT NULL,
email VARCHAR(50) UNIQUE NOT NULL,
cpf VARCHAR(11) UNIQUE NOT NULL,
cargo VARCHAR(150) NOT NULL,
telefone VARCHAR(15),
data_admissao DATE,
data_demissao DATE
);

CREATE TABLE endereco_colaborador (
    id SERIAL PRIMARY KEY,
    colaborador_id INTEGER NOT NULL REFERENCES colaborador(id) ON DELETE CASCADE,
    tipo_endereco VARCHAR(50), -- ex: residencial, comercial
    logradouro VARCHAR(255),
    bairro VARCHAR(255),
    uf VARCHAR(2),
    estado VARCHAR(50),
    cep VARCHAR(20),
    regiao VARCHAR(30)
);