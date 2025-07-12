package br.com.raroacademy.projetofinal.dto.equipamento;

import java.time.LocalDate;
import java.util.List;

import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;

public record EquipamentoRespostaDTO(
		String numeroSerie,
	    String modelo,
	    String marca,
	    LocalDate dataAquisicao,
	    Integer tempoDeUso,
	    STATUS_EQUIPAMENTO status,
	    String tipoEquipamento,
	    List<EspecificacaoRespostaDTO> especificacoes
) {}