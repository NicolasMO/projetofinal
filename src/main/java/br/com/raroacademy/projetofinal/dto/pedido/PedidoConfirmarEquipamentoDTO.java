package br.com.raroacademy.projetofinal.dto.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PedidoConfirmarEquipamentoDTO(
		@NotNull(message = "O ID do equipamento é obrigatório")
	    Long idEquipamento,

	    @NotNull(message = "O número de série é obrigatório")
	    @NotBlank(message = "O número de série não pode estar em branco")
	    String numeroSerie
) {}