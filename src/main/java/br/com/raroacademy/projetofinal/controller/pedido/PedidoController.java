package br.com.raroacademy.projetofinal.controller.pedido;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.raroacademy.projetofinal.dto.pedido.PedidoConfirmarEquipamentoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.pedido.PedidoRespostaPaginaDTO;
import br.com.raroacademy.projetofinal.service.pedido.PedidoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping("/{id}")
    public ResponseEntity<PedidoRespostaDTO> buscarPorId(@PathVariable Long id) {
    	PedidoRespostaDTO resposta = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<String> criarEntrega(@RequestBody @Valid PedidoRequisicaoDTO dto) {
    	pedidoService.solicitarPedido(dto);
        return ResponseEntity.ok("Entrega solicitada com sucesso!");
    }
    
    /*
     * 	Lista entregas que tiveram data de solicitação entre determinado periodo
     */
    @GetMapping("/periodo")
    public ResponseEntity<PedidoRespostaPaginaDTO> listarEntregasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) 
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "dataSolicitacao"));
        PedidoRespostaPaginaDTO resposta = pedidoService.listarPedidosPorPeriodo(inicio, fim, pageable);
        return ResponseEntity.ok(resposta);
    }
    
    
    /*
     * 	Lista os equipamentos que tem previsão de entrega entre determinado periodo
     */
    @GetMapping("/previsao/pendente")
    public ResponseEntity<PedidoRespostaPaginaDTO> listarPendentesPorDataPrevisaoEntrega(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) 
    {     
        Pageable pageable = PageRequest.of(page, size);
        PedidoRespostaPaginaDTO resposta = pedidoService.listarPrevisaoPedidosPendente(inicio, fim, pageable);
        return ResponseEntity.ok(resposta);
    }
    
    /*
     * 	Lista os equipamentos que tem previsão de entrega entre determinado periodo
     *  e tipo de equipamento já existente em banco
     */
    @GetMapping("/previsao/pendente/tipo")
    public ResponseEntity<PedidoRespostaPaginaDTO> listarPendentesPorTipoEPeriodo(
            @RequestParam("tipoEquipamento") String tipoEquipamento,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            Pageable paginacao
            ) 
    {    
    	System.out.println(tipoEquipamento);
    	PedidoRespostaPaginaDTO resposta = pedidoService.listarPrevisaoPedidosPendentePorTipo(tipoEquipamento, inicio, fim, paginacao);
        return ResponseEntity.ok(resposta);
    }
    
    /*
     * 	Lista os equipamentos que tem previsão de entrega entre determinado periodo
     *	e seus equipamentos já chegaram. Por algum motivo só mostra pedidos que estão parcialmente entregues
     */
    @GetMapping("/entregues")
    public ResponseEntity<PedidoRespostaPaginaDTO> listarEntregues(
        @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
        @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
        Pageable paginacao
        ) 
    {
    	PedidoRespostaPaginaDTO resposta = pedidoService.listarEquipamentosEntregues(inicio, fim, paginacao);
        return ResponseEntity.ok(resposta);
    }

    /*
     * Confirmar entrega de equipamento, populando estoque
     */
    @PatchMapping("/confirmar-equipamento")
    public ResponseEntity<String> confirmarEntregaEquipamento(@RequestBody @Valid PedidoConfirmarEquipamentoDTO dto) {
        pedidoService.confirmarEquipamento(dto);
        return ResponseEntity.ok("Equipamento entregue e registrado no estoque.");
    }


}
