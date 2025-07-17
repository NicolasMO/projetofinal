package br.com.raroacademy.projetofinal.mapper.equipamento;

import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento.TipoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;

@Component
public class MapeadorDeTiposEquipamentos {
	
	public static TipoEquipamentoRespostaDTO paraTipoEquipamentoRespostaDTO(TipoEquipamento tipoEquipamento) {
		return new TipoEquipamentoRespostaDTO(
				tipoEquipamento.getId(),
				tipoEquipamento.getNome(),
				tipoEquipamento.getTempoConfiguracao(),
				tipoEquipamento.getEstoqueMinimo()
	        );
	}
	
}
