package br.com.raroacademy.projetofinal.dto.estoque;

public record EstoqueGeralRespostaDTO(
		Long id,
		String tipoEquipamento,
	    Integer estoqueMinimo,
	    Long quantidadeAtual
) {}
