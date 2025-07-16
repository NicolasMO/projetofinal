package br.com.raroacademy.projetofinal.dto.colaborador;

import jakarta.validation.constraints.*;

public record EnderecoRequisicaoDTO(

        @NotBlank(message = "O CEP é obrigatório.")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Formato do CEP inválido. Ex: 70000-000")
        String cep,

        String tipo_endereco,

        String logradouro,

        String bairro,

        String uf,

        String estado,

        String regiao
) {}