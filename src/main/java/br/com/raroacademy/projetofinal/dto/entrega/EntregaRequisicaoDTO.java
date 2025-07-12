package br.com.raroacademy.projetofinal.dto.entrega;

import java.time.LocalDate;
import java.util.List;

public record EntregaRequisicaoDTO(
		LocalDate dataSolicitacao,
		LocalDate dataPrevisaoEntrega,
	    List<EquipamentoEntregaDTO> equipamentos
) {}
