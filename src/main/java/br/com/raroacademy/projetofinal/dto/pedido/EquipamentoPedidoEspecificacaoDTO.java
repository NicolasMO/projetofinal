package br.com.raroacademy.projetofinal.dto.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoPedidoEspecificacaoDTO(
		@NotNull(message = "A descrição da especificação é obrigatória")
	    @NotBlank(message = "A descrição da especificação não pode estar em branco")
	    String descricao,

	    @NotNull(message = "O valor da especificação é obrigatório")
	    @NotBlank(message = "O valor da especificação não pode estar em branco")
	    String valor
) {}
