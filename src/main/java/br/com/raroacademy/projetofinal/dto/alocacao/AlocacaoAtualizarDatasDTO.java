package br.com.raroacademy.projetofinal.dto.alocacao;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AlocacaoAtualizarDatasDTO(
        @NotNull
        LocalDate data
) {}
