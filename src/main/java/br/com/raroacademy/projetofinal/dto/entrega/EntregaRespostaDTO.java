package br.com.raroacademy.projetofinal.dto.entrega;

import java.time.LocalDate;
import java.util.List;

public record EntregaRespostaDTO(
		Long id,
	    LocalDate dataSolicitacao,
	    LocalDate dataPrevisaoEntrega,
	    LocalDate dataEntrega,
	    String status,
	    List<EquipamentoEntregaDTO> equipamentos
) {}
