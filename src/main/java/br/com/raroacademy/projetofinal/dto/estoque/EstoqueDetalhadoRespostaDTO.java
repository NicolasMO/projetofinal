package br.com.raroacademy.projetofinal.dto.estoque;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRespostaDTO;

public record EstoqueDetalhadoRespostaDTO(
		Long id,
	    String tipoEquipamento,
	    Integer estoqueMinimo,
	    Long quantidadeEstoqueAtual,
	    @JsonInclude(JsonInclude.Include.NON_NULL)
	    Long quantidadeEstoqueStatus,
	    List<EquipamentoRespostaDTO> equipamentos
) {}
