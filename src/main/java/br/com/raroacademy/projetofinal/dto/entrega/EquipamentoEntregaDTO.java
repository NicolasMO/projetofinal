package br.com.raroacademy.projetofinal.dto.entrega;

import java.util.List;

public record EquipamentoEntregaDTO(
		String modelo,
	    String marca,
	    String tipoEquipamento,
	    boolean entregue,
	    List<EquipamentoEntregaEspecificacaoDTO> especificacoes
) {}
