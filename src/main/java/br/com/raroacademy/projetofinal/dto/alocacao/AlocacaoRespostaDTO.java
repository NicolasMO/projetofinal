package br.com.raroacademy.projetofinal.dto.alocacao;

import java.time.LocalDate;

public record AlocacaoRespostaDTO(
        Long id,
        String equipamentoId,
        Long colaboradorId,
        LocalDate dataEnvio,
        LocalDate dataEntrega,
        LocalDate dataEntregaPrevista,
        LocalDate dataDevolucao,
        LocalDate dataDevolucaoPrevista
) {}
