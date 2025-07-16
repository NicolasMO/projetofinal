package br.com.raroacademy.projetofinal.dto.pedido;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoRequisicaoDTO(
		@NotNull(message = "A data de solicitação é obrigatória")
	    LocalDate dataSolicitacao,

	    LocalDate dataPrevisaoEntrega,

	    @NotNull(message = "A lista de equipamentos é obrigatória")
	    @Size(min = 1, message = "A entrega deve conter ao menos um equipamento")
	    List<@Valid EquipamentoPedidoDTO> equipamentos
) {}
