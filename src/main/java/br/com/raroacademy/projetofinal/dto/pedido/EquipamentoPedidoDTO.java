package br.com.raroacademy.projetofinal.dto.pedido;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipamentoPedidoDTO(
		Long equipamentoId,
		
	    @NotNull(message = "O modelo é obrigatório")
	    @NotBlank(message = "O modelo não pode estar em branco")
	    String modelo,

	    @NotNull(message = "A marca é obrigatória")
	    @NotBlank(message = "A marca não pode estar em branco")
	    String marca,

	    @NotNull(message = "O tipo de equipamento é obrigatório")
	    @NotBlank(message = "O tipo de equipamento não pode estar em branco")
	    String tipoEquipamento,

	    boolean entregue,
	    
	    @NotNull(message = "A lista de especificações é obrigatória")
	    @Size(min = 1, message = "Cada equipamento deve conter ao menos uma especificação")
	    List<@Valid EquipamentoPedidoEspecificacaoDTO> especificacoes
) {}
