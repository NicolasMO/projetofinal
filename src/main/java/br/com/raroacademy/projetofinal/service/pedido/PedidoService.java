package br.com.raroacademy.projetofinal.service.pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoConfirmarEquipamentoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaPaginaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoTotalEquipamentosPorPeriodoDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_PEDIDO;
import br.com.raroacademy.projetofinal.mapper.equipamento.MapeadorDeEquipamentos;
import br.com.raroacademy.projetofinal.mapper.pedido.MapeadorDePedidos;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedido;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;
import br.com.raroacademy.projetofinal.service.pedido.auxiliar.AuxiliarPedidoService;

@Service
public class PedidoService {
	
	@Autowired
	private AuxiliarPedidoService auxiliarPedidoService;

	@Autowired
	private MapeadorDePedidos mapeadorDePedidos;

	public void solicitarPedido(PedidoRequisicaoDTO dto) {
		Pedido pedido = new Pedido(dto.dataSolicitacao(), dto.dataSolicitacao().plusDays(7), STATUS_PEDIDO.SOLICITADO);

		Set<EquipamentoPedido> equipamentos = dto.equipamentos().stream()
            	.map(equipDTO -> mapeadorDePedidos.paraEntidadeEquipamentoPedido(equipDTO, pedido))
	            .collect(Collectors.toSet());

		pedido.setEquipamentos(equipamentos);

		auxiliarPedidoService.salvarPedido(pedido);
	}

	public PedidoRespostaDTO buscarPorId(Long id) {
		Pedido pedido = auxiliarPedidoService.buscarPedidoPorId(id);
		return mapeadorDePedidos.paraPedidoRespostaDTO(pedido);
	}
	
	public PedidoRespostaPaginaDTO listarPedidosPorPeriodo(LocalDate inicio, LocalDate fim, Pageable paginacao) {
		auxiliarPedidoService.validarIntervaloDatas(inicio, fim);

		Page<Pedido> pedidos = auxiliarPedidoService.buscarPedidosSolicitadosPorPeriodo(inicio, fim, paginacao);
		List<PedidoRespostaDTO> pedidosDTO = auxiliarPedidoService.mapearEValidarPedidos(pedidos, mapeadorDePedidos::paraPedidoRespostaDTO);

		PedidoTotalEquipamentosPorPeriodoDTO total = auxiliarPedidoService.calcularTotalSolicitado(inicio, fim);

		return MapeadorDePedidos.montarPagina(total.totalSolicitados(), total.totalEntregues(), total.totalPendentes(),
				pedidosDTO, pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarPrevisaoPedidosPendente(LocalDate inicio, LocalDate fim, Pageable paginacao) {
		auxiliarPedidoService.validarIntervaloDatas(inicio, fim);

		Page<Pedido> pedidos = auxiliarPedidoService.buscarPedidosPendentesNoPeriodo(inicio, fim, paginacao);
		List<PedidoRespostaDTO> pedidosDTO = auxiliarPedidoService.mapearEValidarPedidos(pedidos, mapeadorDePedidos::paraPedidoRespostaDTO);

		PedidoTotalEquipamentosPorPeriodoDTO total = auxiliarPedidoService.calcularTotalPrevisto(inicio, fim);

		return MapeadorDePedidos.montarPagina(total.totalSolicitados(), total.totalEntregues(), total.totalPendentes(), 
				pedidosDTO, pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public PedidoRespostaPaginaDTO listarEquipamentosEntregues(LocalDate inicio, LocalDate fim, Pageable paginacao) {
		auxiliarPedidoService.validarIntervaloDatas(inicio, fim);

		Page<Pedido> pedidos = auxiliarPedidoService.buscarPedidosNoPeriodoComEquipamentosEntregues(inicio, fim, paginacao);
		List<PedidoRespostaDTO> pedidosDTO = auxiliarPedidoService.mapearEValidarPedidos(pedidos, mapeadorDePedidos::paraPedidoRespostaDTO);

		PedidoTotalEquipamentosPorPeriodoDTO total = auxiliarPedidoService.calcularTotalPrevisto(inicio, fim);

		return MapeadorDePedidos.montarPagina(total.totalSolicitados(), total.totalEntregues(), total.totalPendentes(),
				pedidosDTO, pedidos.getNumber(), pedidos.getTotalPages(), pedidos.getTotalElements());
	}

	public EquipamentoRespostaDTO confirmarEquipamento(PedidoConfirmarEquipamentoDTO dto) {
		EquipamentoPedido equipamentoPedido = auxiliarPedidoService.buscarEquipamentoEntregaPorId(dto.idEquipamento());
		auxiliarPedidoService.validacaoEquipamentoConfirmado(dto, equipamentoPedido);

		Equipamento equipamento = mapeadorDePedidos.paraEntidadeEquipamento(equipamentoPedido, dto.numeroSerie());	
		auxiliarPedidoService.confirmarDevolucaoEquipamento(equipamento);
		
		auxiliarPedidoService.verificacaoEquipamentosEntreguesDoPedido(equipamentoPedido);
		return MapeadorDeEquipamentos.paraEquipamentoRespostaDTO(equipamento);
	}
	
}
