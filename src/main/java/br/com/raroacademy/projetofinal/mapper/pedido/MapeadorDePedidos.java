package br.com.raroacademy.projetofinal.mapper.pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoEspecificacaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.enums.STATUS_PEDIDO;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedido;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedidoEspecificacao;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;
import br.com.raroacademy.projetofinal.service.equipamento.auxiliar.AuxiliarEspecificacaoService;
import br.com.raroacademy.projetofinal.service.equipamento.auxiliar.AuxiliarTipoEquipamentoService;

@Component
public class MapeadorDePedidos {
	
	@Autowired
	private static AuxiliarTipoEquipamentoService auxiliarTipoEquipamentoService;
	
	@Autowired
	private static AuxiliarEspecificacaoService auxiliarEspecificacaoService;
	
	public static Equipamento paraEntidadeEquipamento(EquipamentoPedido equipamentoPedido, String numeroSerie) {
        Set<Especificacao> especificacoes = equipamentoPedido.getEspecificacoes().stream()
        		.map(espec -> new Especificacao(espec.getDescricao(), espec.getValor()))
        		.collect(Collectors.toSet());

        return new Equipamento(
        		numeroSerie,
        		equipamentoPedido.getMarca(),
        		equipamentoPedido.getModelo(),
        		LocalDate.now(),
        		0,
        		STATUS_EQUIPAMENTO.DISPONIVEL,
        		equipamentoPedido.getTipoEquipamento(),
        		especificacoes
    		);
    }
	
	public Pedido criaOPedidoComEquipamentos(PedidoRequisicaoDTO dto) {
        Pedido pedido = new Pedido(dto.dataSolicitacao(), dto.dataSolicitacao().plusDays(7), STATUS_PEDIDO.SOLICITADO);

        Set<EquipamentoPedido> equipamentos = dto.equipamentos().stream()
            .map(equipDTO -> paraEntidadeEquipamentoPedido(equipDTO, pedido))
            .collect(Collectors.toSet());

        pedido.setEquipamentos(equipamentos);
        return pedido;
    }

    public static EquipamentoPedido paraEntidadeEquipamentoPedido(EquipamentoPedidoDTO equipDTO, Pedido pedido) {
        TipoEquipamento tipoEquipamento = auxiliarTipoEquipamentoService.buscarTipoPorNome(equipDTO.tipoEquipamento());

        EquipamentoPedido equipamentoPedido = new EquipamentoPedido(pedido, equipDTO.marca(), equipDTO.modelo(), tipoEquipamento);

        Set<EquipamentoPedidoEspecificacao> especificacoes = equipDTO.especificacoes().stream()
            .map(especDTO -> mapearEspecificacao(especDTO, equipamentoPedido))
            .collect(Collectors.toSet());

        equipamentoPedido.setEspecificacoes(especificacoes);

        return equipamentoPedido;
    }

    private static EquipamentoPedidoEspecificacao mapearEspecificacao(EquipamentoPedidoEspecificacaoDTO especDTO, EquipamentoPedido equipamentoPedido) {
        Especificacao espec = auxiliarEspecificacaoService.buscarEspecificacaoPorDescricaoEValor(especDTO.descricao(), especDTO.valor());
        return new EquipamentoPedidoEspecificacao(espec.getDescricao(), espec.getValor(), equipamentoPedido);
    }
	
	
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
