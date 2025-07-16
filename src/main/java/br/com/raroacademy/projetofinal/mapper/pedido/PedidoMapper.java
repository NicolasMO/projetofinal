package br.com.raroacademy.projetofinal.mapper.pedido;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoEspecificacaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedido;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedidoEspecificacao;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;

@Component
public class PedidoMapper {
	
	public PedidoRespostaDTO paraPedidoRespostaDTO(Pedido pedido) {
		List<EquipamentoPedidoDTO> equipamentosDTO = pedido.getEquipamentos()
				.stream()
				.map(this::paraEquipamentoPedidoDTO)
				.collect(Collectors.toList());
		
		return new PedidoRespostaDTO(
				pedido.getId(),
				pedido.getDataSolicitacao(),
				pedido.getDataPrevisaoEntrega(),
				pedido.getDataEntrega(),
				pedido.getStatus().name(),
				equipamentosDTO
			);
	}
	
	public EquipamentoPedidoDTO paraEquipamentoPedidoDTO(EquipamentoPedido equipamento) {
        List<EquipamentoPedidoEspecificacaoDTO> especificacoesDTO = equipamento.getEspecificacoes().stream()
            .	map(this::paraEquipamentoPedidoEspecificacaoDTO)
            	.collect(Collectors.toList());

        return new EquipamentoPedidoDTO(
        		equipamento.getId(),
        		equipamento.getModelo(),
        		equipamento.getMarca(),
        		equipamento.getTipoEquipamento().getNome(),
        		equipamento.isEntregue(),
	            especificacoesDTO
        );
    }

    public EquipamentoPedidoEspecificacaoDTO paraEquipamentoPedidoEspecificacaoDTO(EquipamentoPedidoEspecificacao espec) {
        return new EquipamentoPedidoEspecificacaoDTO(
	            espec.getDescricao(),
	            espec.getValor()
        );
    }
}
