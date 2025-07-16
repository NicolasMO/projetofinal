package br.com.raroacademy.projetofinal.service.pedido;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoConfirmarEquipamentoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaPaginaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.enums.STATUS_PEDIDO;
import br.com.raroacademy.projetofinal.exception.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.equipamento.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.exception.equipamento.NumeroSerieDuplicadoException;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.pedido.EquipamentoJaEntregueException;
import br.com.raroacademy.projetofinal.exception.pedido.NenhumPedidoEncontradoException;
import br.com.raroacademy.projetofinal.mapper.pedido.PedidoMapper;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedido;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedidoEspecificacao;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.EspecificacaoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.pedido.PedidoRepository;
import br.com.raroacademy.projetofinal.service.builder.pedido.PedidoRespostaBuilder;
import jakarta.validation.Valid;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private TipoEquipamentoRepository tipoEquipamentoRepository;

	@Autowired
	private EquipamentoRepository equipamentoRepository;

	@Autowired
	private EspecificacaoRepository especificacaoRepository;

	@Autowired
	private PedidoRespostaBuilder pedidoRespostaBuilder;

	@Autowired
	private PedidoMapper pedidoMapper;

	public PedidoRespostaDTO buscarPorId(Long id) {
		Pedido pedido = pedidoRepository.findById(id)
				.orElseThrow(() -> new NenhumPedidoEncontradoException("Pedido com ID " + id + " não encontrada."));

		return pedidoMapper.paraPedidoRespostaDTO(pedido);
	}

		public void solicitarPedido(PedidoRequisicaoDTO dto) {
			Pedido pedido = new Pedido();
			pedido.setDataSolicitacao(dto.dataSolicitacao());
			pedido.setStatus(STATUS_PEDIDO.SOLICITADO);
			pedido.setDataPrevisaoEntrega(dto.dataSolicitacao().plusDays(7));
	
			Set<EquipamentoPedido> equipamentos = new HashSet<>();
	
			for (EquipamentoPedidoDTO equipDTO : dto.equipamentos()) {
				TipoEquipamento tipoEquipamento = tipoEquipamentoRepository.findByNomeIgnoreCase(equipDTO.tipoEquipamento())
						.orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(null));
	
				EquipamentoPedido equipamentoPedido = equipamentoPedidoBuilder(pedido, equipDTO, tipoEquipamento);
	
				Set<EquipamentoPedidoEspecificacao> especificacoes = equipDTO.especificacoes().stream().map(especDTO -> {
					Especificacao especExistente = especificacaoRepository
							.findByDescricaoIgnoreCaseAndValorIgnoreCase(especDTO.descricao(), especDTO.valor())
							.orElseThrow(() -> new EspecificacaoNaoEncontradaException("Especificação '"
									+ especDTO.descricao() + ": " + especDTO.valor() + "' não encontrada."));
	
					EquipamentoPedidoEspecificacao espec = new EquipamentoPedidoEspecificacao();
					espec.setDescricao(especExistente.getDescricao());
					espec.setValor(especExistente.getValor());
					espec.setEquipamentoPedido(equipamentoPedido);
					return espec;
				}).collect(Collectors.toSet());
	
				equipamentoPedido.setEspecificacoes(especificacoes);
	
				equipamentos.add(equipamentoPedido);
			}
	
			pedido.setEquipamentos(equipamentos);
	
			pedidoRepository.save(pedido);
		}

		private EquipamentoPedido equipamentoPedidoBuilder(Pedido pedido, EquipamentoPedidoDTO equipDTO,
				TipoEquipamento tipoEquipamento) {
			EquipamentoPedido equipamentoPedido = new EquipamentoPedido();
			equipamentoPedido.setPedido(pedido);
			equipamentoPedido.setModelo(equipDTO.modelo());
			equipamentoPedido.setMarca(equipDTO.marca());
			equipamentoPedido.setTipoEquipamento(tipoEquipamento);
			return equipamentoPedido;
		}

	public PedidoRespostaPaginaDTO listarPedidosPorPeriodo(LocalDate inicio, LocalDate fim, Pageable pageable) {
		validarIntervaloDatas(inicio, fim);

		Page<Pedido> pagina = pedidoRepository.findByDataSolicitacaoBetween(inicio, fim, pageable);

		List<PedidoRespostaDTO> pedidosDTO = pagina.map(pedidoMapper::paraPedidoRespostaDTO).getContent();
		
		validacaoPedidosExistentes(pedidosDTO);

		long totalSolicitados = pedidoRepository.contarTotalEquipamentosSolicitadosNoPeriodo(inicio, fim);
		long totalEntregues = pedidoRepository.contarTotalEquipamentosEntreguesNoPeriodo(inicio, fim);
		long totalPendentes = totalSolicitados - totalEntregues;

		return pedidoRespostaBuilder.montarPagina(totalSolicitados, totalEntregues, totalPendentes, pedidosDTO,
				pagina.getNumber(), pagina.getTotalPages(), pagina.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarPrevisaoPedidosPendente(LocalDate inicio, LocalDate fim, Pageable paginacao) {
		validarIntervaloDatas(inicio, fim);

		Page<Pedido> pedidos = pedidoRepository.findPedidosComEquipamentosPendentesNoPeriodo(inicio, fim, paginacao);

		List<PedidoRespostaDTO> pedidosDTO = pedidoRespostaBuilder.montarListaComFiltro(pedidos.stream(),
				equipamento -> !equipamento.entregue());

		validacaoPedidosExistentes(pedidosDTO);

		long totalPrevistos = pedidoRepository.contarEquipamentosComPrevisaoNoPeriodo(inicio, fim);
		long totalEntregues = pedidoRepository.contarEquipamentosEntreguesComPrevisaoNoPeriodo(inicio, fim);
		long totalPendentes = totalPrevistos - totalEntregues;


		return pedidoRespostaBuilder.montarPagina(totalPrevistos, totalEntregues, totalPendentes, pedidosDTO,
				pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarPrevisaoPedidosPendentePorTipo(String tipoEquipamento, LocalDate inicio,
			LocalDate fim, Pageable paginacao) {
		validarIntervaloDatas(inicio, fim);

		TipoEquipamento tipo = tipoEquipamentoRepository.findByNomeIgnoreCase(tipoEquipamento)
				.orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(
						"Tipo de equipamento '" + tipoEquipamento + "' não encontrado."));

		Page<Pedido> pedidos = pedidoRepository.findPedidosPorTipoEquipamentoNoPeriodo(tipo, inicio, fim, paginacao);

		List<PedidoRespostaDTO> pedidosDTO = pedidoRespostaBuilder.montarListaComFiltro(pedidos.stream(),
				equipamento -> equipamento.tipoEquipamento().equalsIgnoreCase(tipo.getNome()));

		validacaoPedidosExistentes(pedidosDTO);

		long totalSolicitados = pedidoRepository.contarTotalEquipamentosSolicitadosNoPeriodo(inicio, fim);
		long totalEntregues = pedidoRepository.contarTotalEquipamentosEntreguesNoPeriodo(inicio, fim);
		long totalPendentes = totalSolicitados - totalEntregues;

		return pedidoRespostaBuilder.montarPagina(totalSolicitados, totalEntregues, totalPendentes, pedidosDTO,
				pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarEquipamentosEntregues(LocalDate inicio, LocalDate fim, Pageable paginacao) {
		validarIntervaloDatas(inicio, fim);

		Page<Pedido> pedidos = pedidoRepository.findPedidosComEquipamentosEntreguesNoPeriodo(inicio, fim, paginacao);

		List<PedidoRespostaDTO> pedidosDTO = pedidoRespostaBuilder.montarListaComFiltro(pedidos.stream(),
				EquipamentoPedidoDTO::entregue);

		validacaoPedidosExistentes(pedidosDTO);

		long totalPrevistos = pedidoRepository.contarEquipamentosComPrevisaoNoPeriodo(inicio, fim);
		long totalEntregues = pedidoRepository.contarEquipamentosEntreguesComPrevisaoNoPeriodo(inicio, fim);
		long totalPendentes = totalPrevistos - totalEntregues;

		return pedidoRespostaBuilder.montarPagina(totalPrevistos, totalEntregues, totalPendentes, pedidosDTO,
				pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public void confirmarEquipamento(@Valid PedidoConfirmarEquipamentoDTO dto) {

		EquipamentoPedido equipamentoPedido = pedidoRepository.buscarEquipamentoEntregaPorId(dto.idEquipamento())
				.orElseThrow(() -> new EquipamentoNaoEncontradoException("Equipamento de entrega não encontrado."));

		validacaoEquipamentoConfirmado(dto, equipamentoPedido);

		equipamentoPedido.setEntregue(true);

		Equipamento equipamento = new Equipamento();
		equipamento.setNumeroSerie(dto.numeroSerie());
		equipamento.setMarca(equipamentoPedido.getMarca());
		equipamento.setModelo(equipamentoPedido.getModelo());
		equipamento.setTipoEquipamento(equipamentoPedido.getTipoEquipamento());
		equipamento.setStatus(STATUS_EQUIPAMENTO.DISPONIVEL);
		equipamento.setDataAquisicao(LocalDate.now());

		Set<Especificacao> especificacoes = equipamentoPedido
				.getEspecificacoes().stream().map(
						espec -> especificacaoRepository
								.findByDescricaoIgnoreCaseAndValorIgnoreCase(espec.getDescricao(), espec.getValor())
								.orElseThrow(() -> new EspecificacaoNaoEncontradaException(
										"Especificação não encontrada: " + espec.getDescricao())))
				.collect(Collectors.toSet());

		equipamento.setEspecificacoes(especificacoes);

		equipamentoRepository.save(equipamento);
	}
	
	private void validacaoPedidosExistentes(List<PedidoRespostaDTO> pedidosDTO) {
		if (pedidosDTO.isEmpty()) {
			throw new NenhumPedidoEncontradoException("Não há pedidos no período informado.");
		}
	}

	private void validarIntervaloDatas(LocalDate inicio, LocalDate fim) {
		if (inicio == null || fim == null) {
			throw new IllegalArgumentException("Datas de início e fim devem ser informadas.");
		}
		if (fim.isBefore(inicio)) {
			throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início.");
		}
		long dias = ChronoUnit.DAYS.between(inicio, fim);
		if (dias > 90) {
			throw new IllegalArgumentException("O intervalo máximo permitido é de 90 dias.");
		}
	}
	
	private void validacaoEquipamentoConfirmado(PedidoConfirmarEquipamentoDTO dto,
			EquipamentoPedido equipamentoPedido) {
		if (equipamentoPedido.isEntregue()) {
			throw new EquipamentoJaEntregueException("Esse equipamento já foi dado baixa.");
		}
		
		if (equipamentoRepository.existsById(dto.numeroSerie())) {
		    throw new NumeroSerieDuplicadoException("Já existe um equipamento com o número de série informado.");
		}
	}

}
