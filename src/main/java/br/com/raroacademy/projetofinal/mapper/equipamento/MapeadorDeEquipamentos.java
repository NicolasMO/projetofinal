package br.com.raroacademy.projetofinal.mapper.equipamento;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.especificacao.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;

@Component
public class MapeadorDeEquipamentos {

    public static EquipamentoRespostaDTO paraEquipamentoRespostaDTO(Equipamento equipamento) {
        List<EspecificacaoRespostaDTO> especificacoes = equipamento.getEspecificacoes()
            .stream()
            .map(espec -> new EspecificacaoRespostaDTO(espec.getId(), espec.getDescricao(), espec.getValor()))
            .toList();

        return new EquipamentoRespostaDTO(
            equipamento.getNumeroSerie(),
            equipamento.getModelo(),
            equipamento.getMarca(),
            equipamento.getDataAquisicao(),
            equipamento.getTempoUso(),
            equipamento.getStatus(),
            equipamento.getTipoEquipamento().getNome(),
            especificacoes
        );
    }
    
    public static Equipamento paraEntidadeEquipamento(EquipamentoRequisicaoDTO dto, TipoEquipamento tipo, Set<Especificacao> especificacoes) {
        return new Equipamento(
            dto.numeroSerie(),
            dto.modelo(),
            dto.marca(),
            dto.dataAquisicao(),
            0,
            dto.statusEquipamento(),
            tipo,
            especificacoes
        );
    }
}
