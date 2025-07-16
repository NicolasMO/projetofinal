package br.com.raroacademy.projetofinal.mapper.alocacao;

import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.model.alocacao.AlocacaoEquipamento;

@Component
public class AlocacaoEquipamentoMapper {

    public AlocacaoEquipamentoRespostaDTO paraRespostaDTO(AlocacaoEquipamento alocacaoEquipamento) {
        return new AlocacaoEquipamentoRespostaDTO(
        		alocacaoEquipamento.getId(),
        		alocacaoEquipamento.getEquipamento().getNumeroSerie(),
        		alocacaoEquipamento.getEquipamento().getModelo(),
        		alocacaoEquipamento.getEquipamento().getMarca(),
        		alocacaoEquipamento.getColaborador().getNome(),
        		alocacaoEquipamento.getDataEntregaPrevista(),
        		alocacaoEquipamento.getDataEntrega(),
        		alocacaoEquipamento.getDataDevolucaoPrevista(),
        		alocacaoEquipamento.getDataDevolucao(),
        		alocacaoEquipamento.isDevolvido(),
        		alocacaoEquipamento.getStatusEquipamento(),
                "Registro gerado a partir da movimentação"
        );
    }
}