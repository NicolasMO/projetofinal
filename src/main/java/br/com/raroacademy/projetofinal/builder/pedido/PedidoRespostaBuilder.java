package br.com.raroacademy.projetofinal.builder.pedido;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.pedido.EquipamentoPedidoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaPaginaDTO;
import br.com.raroacademy.projetofinal.mapper.pedido.MapeadorDePedidos;


@Component
public class PedidoRespostaBuilder {

	@Autowired
    private MapeadorDePedidos mapeadorDePedidos;

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
}
