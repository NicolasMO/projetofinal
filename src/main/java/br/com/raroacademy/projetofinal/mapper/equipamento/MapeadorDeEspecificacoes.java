package br.com.raroacademy.projetofinal.mapper.equipamento;

import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.equipamento.especificacao.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;

@Component
public class MapeadorDeEspecificacoes {

	public static EspecificacaoRespostaDTO paraEspecificacaoRespostaDTO(Especificacao especificacao) {
		return new EspecificacaoRespostaDTO(
				especificacao.getId(),
				especificacao.getDescricao(),
				especificacao.getValor()
				);
	}
}
