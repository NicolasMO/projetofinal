package br.com.raroacademy.projetofinal.dto.estoque;

import jakarta.validation.constraints.NotNull;

public record AtualizarEstoqueMinimoDTO(
		@NotNull 
		Integer estoqueMinimo
) {}

