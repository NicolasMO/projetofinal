package br.com.raroacademy.projetofinal.dto.alocacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AlocacaoRequisicaoPorNomeDTO (
        @NotNull @NotBlank
        String numeroSerie,

        @NotNull
        String colaboradorNome,

        LocalDate dataEnvio,

        LocalDate dataEntrega,

        @NotNull
        LocalDate dataEntregaPrevista,

        LocalDate dataDevolucao,

        @NotNull
        LocalDate dataDevolucaoPrevista
) {}
