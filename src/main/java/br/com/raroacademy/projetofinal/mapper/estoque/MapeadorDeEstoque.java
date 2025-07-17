package br.com.raroacademy.projetofinal.mapper.estoque;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.especificacao.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoPaginaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;

@Component
public class MapeadorDeEstoque {
	
	public static List<EstoqueDetalhadoRespostaDTO> mapearPaginaEstoqueDetalhado(Map<Long, List<Equipamento>> agrupadoPorTipo, List<STATUS_EQUIPAMENTO> status) {
	    return agrupadoPorTipo.values().stream()
	        .map(equipamentos -> {
	            long total = equipamentos.size();
	            long totalComStatus = equipamentos.stream()
	                .filter(e -> status == null || status.isEmpty() || status.contains(e.getStatus()))
	                .count();
	            return MapeadorDeEstoque.paraEstoqueDetalhadoRespostaDTO(equipamentos, status, total, totalComStatus);
	        })
	        .toList();
	}
	
	public static EstoqueDetalhadoRespostaDTO paraEstoqueDetalhadoRespostaDTO(List<Equipamento> equipamentos, List<STATUS_EQUIPAMENTO> status, Long total, Long totalComStatus) {
        if (equipamentos == null || equipamentos.isEmpty()) {
            throw new IllegalArgumentException("Lista de equipamentos vazia.");
        }

        TipoEquipamento tipo = equipamentos.get(0).getTipoEquipamento();

        List<EquipamentoRespostaDTO> equipamentosDTO = equipamentos.stream().map(e -> {
            List<EspecificacaoRespostaDTO> especificacoes = e.getEspecificacoes().stream()
                    .map(espec -> new EspecificacaoRespostaDTO(espec.getId(), espec.getDescricao(), espec.getValor()))
                    .collect(Collectors.toList());

            return new EquipamentoRespostaDTO(
                    e.getNumeroSerie(),
                    e.getModelo(),
                    e.getMarca(),
                    e.getDataAquisicao(),
                    e.getTempoUso(),
                    e.getStatus(),
                    tipo.getNome(),
                    especificacoes
            );
        }).toList();

        return new EstoqueDetalhadoRespostaDTO(
                tipo.getId(),
                tipo.getNome(),
                tipo.getEstoqueMinimo(),
                total,
                totalComStatus,
                equipamentosDTO
        );
    }
	
	public static EstoqueDetalhadoPaginaDTO mapearParaPaginaDTO(
			List<EstoqueDetalhadoRespostaDTO> resposta,
		    int numeroPagina,
		    int totalPaginas,
		    long totalElementos,
		    int tamanhoPagina,
		    boolean ultimaPagina
		    ) 
	{
		return new EstoqueDetalhadoPaginaDTO(
		        resposta,
		        numeroPagina,
		        totalPaginas,
		        totalElementos,
		        tamanhoPagina,
		        ultimaPagina
		    );
	}

}
