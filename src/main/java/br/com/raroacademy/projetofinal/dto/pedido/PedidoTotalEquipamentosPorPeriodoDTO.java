package br.com.raroacademy.projetofinal.dto.pedido;

public record PedidoTotalEquipamentosPorPeriodoDTO(
		long totalSolicitados, 
		long totalEntregues, 
		long totalPendentes
) {}
