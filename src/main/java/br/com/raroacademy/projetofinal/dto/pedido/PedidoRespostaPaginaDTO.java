package br.com.raroacademy.projetofinal.dto.pedido;

import java.util.List;

public record PedidoRespostaPaginaDTO(
		long totalEquipamentosSolicitados,
	    long totalEquipamentosEntregues,
	    long totalEquipamentosPendentes,
		List<PedidoRespostaDTO> pedidos,
	    int paginaAtual,
	    int totalPaginas,
	    long totalElementos	    
) {}
