package br.com.raroacademy.projetofinal.dto.equipamento;

import jakarta.validation.constraints.NotNull;

public record AtualizarTipoEquipamentoEstoqueMinimoDTO(
		@NotNull 
		Integer estoqueMinimo
) {}

