package br.com.raroacademy.projetofinal.service.pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.builder.pedido.PedidoRespostaBuilder;
import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoEspecificacaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoConfirmarEquipamentoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaPaginaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_PEDIDO;
import br.com.raroacademy.projetofinal.exception.equipamento.especificacao.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.exception.equipamento.tipoEquipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.mapper.pedido.MapeadorDePedidos;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedido;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedidoEspecificacao;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;
import br.com.raroacademy.projetofinal.repository.equipamento.EspecificacaoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.service.pedido.auxiliar.AuxiliarPedidoService;
import jakarta.validation.Valid;

@Service
public class PedidoService {

	@Autowired
	private AuxiliarPedidoService auxiliarPedidoService;
	
	

	@Autowired
	private TipoEquipamentoRepository tipoEquipamentoRepository;


	@Autowired
	private EspecificacaoRepository especificacaoRepository;

	@Autowired
	private PedidoRespostaBuilder pedidoRespostaBuilder;

	@Autowired
	private MapeadorDePedidos mapeadorDePedidos;

	public PedidoRespostaDTO buscarPorId(Long id) {
		Pedido pedido = auxiliarPedidoService.buscarPedidoPorId(id);
		return mapeadorDePedidos.paraPedidoRespostaDTO(pedido);
	}

	public void solicitarPedido(PedidoRequisicaoDTO dto) {
		Pedido pedido = new Pedido(dto.dataSolicitacao(), dto.dataPrevisaoEntrega(), STATUS_PEDIDO.SOLICITADO);

		Set<EquipamentoPedido> equipamentos = dto.equipamentos().stream()
	            .map(equipDTO -> MapeadorDePedidos.paraEntidadeEquipamentoPedido(equipDTO, pedido))
	            .collect(Collectors.toSet());

		pedido.setEquipamentos(equipamentos);

		auxiliarPedidoService.salvarPedido(pedido);
	}

	public PedidoRespostaPaginaDTO listarPedidosPorPeriodo(LocalDate inicio, LocalDate fim, Pageable pageable) {
		auxiliarPedidoService.validarIntervaloDatas(inicio, fim);

		Page<Pedido> pagina = auxiliarPedidoService.buscarPedidosSolicitadosPorPeriodo(inicio, fim, pageable);

		List<PedidoRespostaDTO> pedidosDTO = pagina.map(mapeadorDePedidos::paraPedidoRespostaDTO).getContent();
		
		auxiliarPedidoService.validacaoPedidosExistentes(pedidosDTO);

		long totalSolicitados = auxiliarPedidoService.contarTotalEquipamentosSolicitadosNoPeriodo(inicio, fim);
		long totalEntregues = auxiliarPedidoService.contarTotalEquipamentosEntreguesNoPeriodo(inicio, fim);
		long totalPendentes = totalSolicitados - totalEntregues;

		return pedidoRespostaBuilder.montarPagina(totalSolicitados, totalEntregues, totalPendentes, pedidosDTO,
				pagina.getNumber(), pagina.getTotalPages(), pagina.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarPrevisaoPedidosPendente(LocalDate inicio, LocalDate fim, Pageable paginacao) {
		auxiliarPedidoService.validarIntervaloDatas(inicio, fim);

		Page<Pedido> pedidos = auxiliarPedidoService.buscarPedidosPendentesNoPeriodo(inicio, fim, paginacao);

		List<PedidoRespostaDTO> pedidosDTO = pedidos.stream().map(mapeadorDePedidos::paraPedidoRespostaDTO).collect(Collectors.toList());

		auxiliarPedidoService.validacaoPedidosExistentes(pedidosDTO);

		long totalPrevistos = auxiliarPedidoService.contarEquipamentosComPrevisaoNoPeriodo(inicio, fim);
		long totalEntregues = auxiliarPedidoService.contarEquipamentosEntreguesComPrevisaoNoPeriodo(inicio, fim);
		long totalPendentes = totalPrevistos - totalEntregues;


		return pedidoRespostaBuilder.montarPagina(totalPrevistos, totalEntregues, totalPendentes, pedidosDTO,
				pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarPrevisaoPedidosPendentePorTipo(String tipoEquipamento, LocalDate inicio,
			LocalDate fim, Pageable paginacao) {
		auxiliarPedidoService.validarIntervaloDatas(inicio, fim);

		TipoEquipamento tipo = tipoEquipamentoRepository.findByNomeIgnoreCase(tipoEquipamento)
				.orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(
						"Tipo de equipamento '" + tipoEquipamento + "' não encontrado."));

		Page<Pedido> pedidos = auxiliarPedidoService.buscarPedidosNoPeriodoPorTipoEquipamento(tipo, inicio, fim, paginacao);

		List<PedidoRespostaDTO> pedidosDTO = pedidos.stream().map(mapeadorDePedidos::paraPedidoRespostaDTO).collect(Collectors.toList());

		auxiliarPedidoService.validacaoPedidosExistentes(pedidosDTO);

		long totalSolicitados = auxiliarPedidoService.contarTotalEquipamentosSolicitadosNoPeriodo(inicio, fim);
		long totalEntregues = auxiliarPedidoService.contarTotalEquipamentosEntreguesNoPeriodo(inicio, fim);
		long totalPendentes = totalSolicitados - totalEntregues;

		return pedidoRespostaBuilder.montarPagina(totalSolicitados, totalEntregues, totalPendentes, pedidosDTO,
				pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarEquipamentosEntregues(LocalDate inicio, LocalDate fim, Pageable paginacao) {
		auxiliarPedidoService.validarIntervaloDatas(inicio, fim);

		Page<Pedido> pedidos = auxiliarPedidoService.buscarPedidosNoPeriodoComEquipamentosEntregues(inicio, fim, paginacao);

		List<PedidoRespostaDTO> pedidosDTO = pedidos.stream().map(mapeadorDePedidos::paraPedidoRespostaDTO).collect(Collectors.toList());
		
		auxiliarPedidoService.validacaoPedidosExistentes(pedidosDTO);

		long totalPrevistos = auxiliarPedidoService.contarEquipamentosComPrevisaoNoPeriodo(inicio, fim);
		long totalEntregues = auxiliarPedidoService.contarEquipamentosEntreguesComPrevisaoNoPeriodo(inicio, fim);
		long totalPendentes = totalPrevistos - totalEntregues;

		return pedidoRespostaBuilder.montarPagina(totalPrevistos, totalEntregues, totalPendentes, pedidosDTO,
				pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public void confirmarEquipamento(@Valid PedidoConfirmarEquipamentoDTO dto) {
		EquipamentoPedido equipamentoPedido = auxiliarPedidoService.buscarEquipamentoEntregaPorId(dto.idEquipamento());
		auxiliarPedidoService.validacaoEquipamentoConfirmado(dto, equipamentoPedido);
		equipamentoPedido.setEntregue(true);

		Equipamento equipamento = MapeadorDePedidos.paraEntidadeEquipamento(equipamentoPedido, dto.numeroSerie());	
		auxiliarPedidoService.confirmarDevolucaoEquipamento(equipamento);
		
		verificacaoEquipamentosEntreguesPedido(equipamentoPedido);
	}

	private void verificacaoEquipamentosEntreguesPedido(EquipamentoPedido equipamentoPedido) {
		Pedido pedido = equipamentoPedido.getPedido();

	    boolean todosEntregues = pedido.getEquipamentos().stream().allMatch(EquipamentoPedido::isEntregue);

	    if (todosEntregues) {
	        pedido.setStatus(STATUS_PEDIDO.ENTREGUE);
	        if (pedido.getDataEntrega() == null) {
	            pedido.setDataEntrega(LocalDate.now());
	        }
	        auxiliarPedidoService.salvarPedido(pedido);
	    }
	}
	
	private EquipamentoPedido criarEquipamentoPedido(EquipamentoPedidoDTO equipDTO, Pedido pedido) {
	    TipoEquipamento tipoEquipamento = tipoEquipamentoRepository
	        .findByNomeIgnoreCase(equipDTO.tipoEquipamento())
	        .orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(null));

	    EquipamentoPedido equipamentoPedido = new EquipamentoPedido(
    		pedido,
	        equipDTO.marca(),
	        equipDTO.modelo(),
	        tipoEquipamento
	    );

	    Set<EquipamentoPedidoEspecificacao> especificacoes = equipDTO.especificacoes().stream()
	        .map(especDTO -> criarEspecificacao(especDTO, equipamentoPedido))
	        .collect(Collectors.toSet());

	    equipamentoPedido.setEspecificacoes(especificacoes);
	    return equipamentoPedido;
	}

	private EquipamentoPedidoEspecificacao criarEspecificacao(EquipamentoPedidoEspecificacaoDTO especDTO, EquipamentoPedido equipamentoPedido) {
	    Especificacao especExistente = especificacaoRepository
	        .findByDescricaoIgnoreCaseAndValorIgnoreCase(especDTO.descricao(), especDTO.valor())
	        .orElseThrow(() -> new EspecificacaoNaoEncontradaException(
	            "Especificação '" + especDTO.descricao() + ": " + especDTO.valor() + "' não encontrada."
	        ));

	    return new EquipamentoPedidoEspecificacao(
	        especExistente.getDescricao(),
	        especExistente.getValor(),
	        equipamentoPedido
	    );
	}


}
