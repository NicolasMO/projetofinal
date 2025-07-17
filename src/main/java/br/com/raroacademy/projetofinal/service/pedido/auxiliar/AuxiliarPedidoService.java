package br.com.raroacademy.projetofinal.service.pedido.auxiliar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.pedido.PedidoConfirmarEquipamentoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_PEDIDO;
import br.com.raroacademy.projetofinal.exception.equipamento.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.equipamento.equipamento.NumeroSerieDuplicadoException;
import br.com.raroacademy.projetofinal.exception.pedido.EquipamentoJaEntregueException;
import br.com.raroacademy.projetofinal.exception.pedido.NenhumPedidoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedido;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.pedido.PedidoRepository;

@Component
public class AuxiliarPedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EquipamentoRepository equipamentoRepository;
	
	public void salvarPedido(Pedido pedido) {
		pedidoRepository.save(pedido);
	}
	
	public void confirmarDevolucaoEquipamento(Equipamento equipamento) {
		equipamentoRepository.save(equipamento);
	}
	
	public Pedido buscarPedidoPorId(Long id) {
		return pedidoRepository.findById(id)
				.orElseThrow(() -> new NenhumPedidoEncontradoException("Pedido com ID " + id + " não encontrado."));
	}
	

	public Page<Pedido> buscarPedidosSolicitadosPorPeriodo(LocalDate inicio, LocalDate fim, Pageable pageable) {
		return pedidoRepository.findByDataSolicitacaoBetween(inicio, fim, pageable);
	}
	
	public long contarTotalEquipamentosSolicitadosNoPeriodo(LocalDate inicio, LocalDate fim) {
		return pedidoRepository.contarTotalEquipamentosSolicitadosNoPeriodo(inicio, fim);
	}

	public long contarTotalEquipamentosEntreguesNoPeriodo(LocalDate inicio, LocalDate fim) {
		return pedidoRepository.contarTotalEquipamentosEntreguesNoPeriodo(inicio, fim);
	}
	
	public Page<Pedido> buscarPedidosPendentesNoPeriodo(LocalDate inicio, LocalDate fim, Pageable pageable) {
		return pedidoRepository.findPedidosComEquipamentosPendentesNoPeriodo(inicio, fim, pageable);
	}

	public long contarEquipamentosComPrevisaoNoPeriodo(LocalDate inicio, LocalDate fim) {
		return pedidoRepository.contarEquipamentosComPrevisaoNoPeriodo(inicio, fim);
	}

	public long contarEquipamentosEntreguesComPrevisaoNoPeriodo(LocalDate inicio, LocalDate fim) {
		return pedidoRepository.contarEquipamentosEntreguesComPrevisaoNoPeriodo(inicio, fim);
	}
	
	public Page<Pedido> buscarPedidosNoPeriodoPorTipoEquipamento(TipoEquipamento tipo, LocalDate inicio, LocalDate fim, Pageable pageable) {
		return pedidoRepository.findPedidosPorTipoEquipamentoNoPeriodo(tipo, inicio, fim, pageable);
	}

	public Page<Pedido> buscarPedidosNoPeriodoComEquipamentosEntregues(LocalDate inicio, LocalDate fim, Pageable pageable) {
		return pedidoRepository.findPedidosComEquipamentosEntreguesNoPeriodo(inicio, fim, pageable);
	}

	public EquipamentoPedido buscarEquipamentoEntregaPorId(Long id) {
		return pedidoRepository.buscarEquipamentoEntregaPorId(id)
				.orElseThrow(() -> new EquipamentoNaoEncontradoException("Equipamento de entrega não encontrado."));
	}
	
	public void verificacaoEquipamentosEntreguesPedido(EquipamentoPedido equipamentoPedido) {
		Pedido pedido = equipamentoPedido.getPedido();

	    boolean todosEntregues = pedido.getEquipamentos().stream().allMatch(EquipamentoPedido::isEntregue);

	    if (todosEntregues) {
	        pedido.setStatus(STATUS_PEDIDO.ENTREGUE);
	        if (pedido.getDataEntrega() == null) {
	            pedido.setDataEntrega(LocalDate.now());
	        }
	        pedidoRepository.save(pedido);
	    }
	}
	
	public void validacaoPedidosExistentes(List<PedidoRespostaDTO> pedidosDTO) {
		if (pedidosDTO.isEmpty()) {
			throw new NenhumPedidoEncontradoException("Não há pedidos no período informado.");
		}
	}

	public void validarIntervaloDatas(LocalDate inicio, LocalDate fim) {
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
	
	public void validacaoEquipamentoConfirmado(PedidoConfirmarEquipamentoDTO dto,
			EquipamentoPedido equipamentoPedido) {
		if (equipamentoPedido.isEntregue()) {
			throw new EquipamentoJaEntregueException("Esse equipamento já foi dado baixa.");
		}
		
		if (equipamentoRepository.existsById(dto.numeroSerie())) {
		    throw new NumeroSerieDuplicadoException("Já existe um equipamento com o número de série informado.");
		}
	}

}
