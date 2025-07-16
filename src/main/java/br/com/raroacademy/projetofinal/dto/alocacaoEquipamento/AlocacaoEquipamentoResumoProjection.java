package br.com.raroacademy.projetofinal.dto.alocacaoEquipamento;

public interface AlocacaoEquipamentoResumoProjection{
		Long getTotalPrevisto();
		Long getTotalEntregue();
		Long getTotalPendente();
}