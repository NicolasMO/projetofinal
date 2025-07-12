package br.com.raroacademy.projetofinal.dto.estoque;

import java.util.List;

public record EstoqueDetalhadoPaginaDTO(
		List<EstoqueDetalhadoRespostaDTO> content,
	    int paginaAtual,
	    int totalPaginas,
	    long totalElementos,
	    int tamanhoPagina,
	    boolean ultimaPagina
) {}
