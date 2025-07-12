package br.com.raroacademy.projetofinal.dto.equipamento;

public record TipoEquipamentoRespostaDTO(
		Long id,
        String nome,
        Integer tempoConfiguracao,
        Integer estoqueMinimo
) {}
