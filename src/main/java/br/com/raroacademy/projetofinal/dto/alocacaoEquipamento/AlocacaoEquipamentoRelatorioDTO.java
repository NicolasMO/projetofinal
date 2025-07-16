package br.com.raroacademy.projetofinal.dto.alocacaoEquipamento;

import java.util.List;

public record AlocacaoEquipamentoRelatorioDTO(
		long totalPrevisto,
	    long totalDevolvido,
	    long totalPendente,
	    int paginaAtual,
	    int totalPaginas,
	    long totalElementos,
	    int quantidadePorPagina,
	    List<AlocacaoEquipamentoRespostaDTO> detalhes
) {}
