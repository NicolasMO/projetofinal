package br.com.raroacademy.projetofinal.dto.equipamento.especificacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EspecificacaoRequisicaoDTO(
        @NotNull @NotBlank(message = "A descrição é obrigatória")
        String descricao,
        
        @NotNull @NotBlank(message = "O valor é obrigatório")	
        String valor
) {}
