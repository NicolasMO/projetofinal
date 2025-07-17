package br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento;

import jakarta.validation.constraints.NotNull;

public record AtualizarTipoEquipamentoEstoqueMinimoDTO(
		@NotNull 
		Integer estoqueMinimo
) {}

