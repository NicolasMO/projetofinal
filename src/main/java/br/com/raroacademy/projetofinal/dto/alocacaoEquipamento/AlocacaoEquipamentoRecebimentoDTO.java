package br.com.raroacademy.projetofinal.dto.alocacaoEquipamento;

import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlocacaoEquipamentoRecebimentoDTO(
		@NotNull 
		Long alocacaoEquipamentoId,
		@NotNull 
		String numeroSerie,
	    @NotNull 
	    STATUS_EQUIPAMENTO statusEquipamento,
	    @NotBlank 
	    String observacao
) {}