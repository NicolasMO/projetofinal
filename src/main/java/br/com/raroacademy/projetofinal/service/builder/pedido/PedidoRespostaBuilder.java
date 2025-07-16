package br.com.raroacademy.projetofinal.service.builder.pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaPaginaDTO;
import br.com.raroacademy.projetofinal.mapper.pedido.PedidoMapper;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;


@Component
public class PedidoRespostaBuilder {

	@Autowired
    private PedidoMapper pedidoMapper;

	public PedidoRespostaPaginaDTO montarPagina(long totalSolicitados, long totalEntregues, long totalPendentes,
            List<PedidoRespostaDTO> entregasDTO, int numeroPagina, int totalPaginas, long totalElementos
            ) 
	{
            return new PedidoRespostaPaginaDTO(
            		totalSolicitados, 
            		totalEntregues, 
            		totalPendentes,
            		entregasDTO, 
            		numeroPagina, 
            		totalPaginas,
            		totalElementos
            		);
    }
	
	public PedidoRespostaDTO montarEntregaDTO(Long idEntrega, LocalDate dataSolicitacao, LocalDate dataPrevisaoEntrega,
            LocalDate dataEntrega, String status, List<EquipamentoPedidoDTO> equipamentos) 
	{	
		return new PedidoRespostaDTO(
					idEntrega, 
					dataSolicitacao, 
					dataPrevisaoEntrega,
					dataEntrega, 
					status, 
					equipamentos
					);
	}
	
    public PedidoRespostaDTO montarEntregaDTOComFiltro(Pedido pedido, Predicate<EquipamentoPedidoDTO> filtro) {
    		PedidoRespostaDTO dtoEntrega = pedidoMapper.paraPedidoRespostaDTO(pedido);
	
	        List<EquipamentoPedidoDTO> filtrados = dtoEntrega.equipamentos().stream()
	                .filter(filtro)
	                .collect(Collectors.toList());
	
	        return montarEntregaDTO(
	        		dtoEntrega.idPedido(), 
	        		dtoEntrega.dataSolicitacao(), 
	        		dtoEntrega.dataPrevisaoEntrega(),
	        		dtoEntrega.dataEntrega(), 
	        		dtoEntrega.status(), 
	        		filtrados
	        	);
    }

    public List<PedidoRespostaDTO> montarListaComFiltro(Stream<Pedido> pedidos, Predicate<EquipamentoPedidoDTO> filtro) {
        return pedidos
                .map(p -> montarEntregaDTOComFiltro(p, filtro))
                .filter(dto -> !dto.equipamentos().isEmpty())
                .collect(Collectors.toList());
    }

    public long contarEquipamentos(List<PedidoRespostaDTO> entregas) {
        return entregas.stream()
                .mapToLong(dto -> dto.equipamentos().size())
                .sum();
    }

    public long contarEquipamentosFiltrados(List<PedidoRespostaDTO> entregas, Predicate<EquipamentoPedidoDTO> filtro) {
        return entregas.stream()
                .flatMap(dto -> dto.equipamentos().stream())
                .filter(filtro)
                .count();
    }
    
}
