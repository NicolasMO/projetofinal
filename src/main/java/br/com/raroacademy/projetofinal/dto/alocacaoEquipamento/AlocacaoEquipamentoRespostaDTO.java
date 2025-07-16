package br.com.raroacademy.projetofinal.dto.alocacaoEquipamento;

import java.time.LocalDate;
import java.util.List;

import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;

public record AlocacaoEquipamentoRespostaDTO(
		Long id,
	    String numeroSerie,
	    String modelo,
	    String marca,
	    String nomeColaborador,
	    LocalDate dataEntregaPrevista,
	    LocalDate dataEntrega,
	    LocalDate dataDevolucaoPrevista,
	    LocalDate dataDevolucao,
	    boolean devolvido,
	    STATUS_EQUIPAMENTO status,
	    String texto
) {}