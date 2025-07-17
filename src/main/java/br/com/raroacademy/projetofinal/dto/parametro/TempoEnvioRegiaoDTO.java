package br.com.raroacademy.projetofinal.dto.parametro;

import jakarta.validation.constraints.*;

public record TempoEnvioRegiaoDTO(
        @NotBlank(message = "regiao não pode ser vazia") String regiao,
        @NotNull(message = "tempoEnvioDias é obrigatório")
        @Min(value = 0, message = "tempoEnvioDias deve ser zero ou maior")
        Integer tempoEnvioDias
) {}
