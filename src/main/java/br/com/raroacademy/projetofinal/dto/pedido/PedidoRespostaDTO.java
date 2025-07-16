package br.com.raroacademy.projetofinal.dto.pedido;

import java.time.LocalDate;
import java.util.List;

public record PedidoRespostaDTO(
		Long idPedido,
	    LocalDate dataSolicitacao,
	    LocalDate dataPrevisaoEntrega,
	    LocalDate dataEntrega,
	    String status,
	    List<EquipamentoPedidoDTO> equipamentos
) {}
