package br.com.raroacademy.projetofinal.controller.pedido;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRespostaDTO;
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

    @GetMapping("/periodo")
    public ResponseEntity<PedidoRespostaPaginaDTO> listarEntregasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            Pageable paginacao
            ) 
    {
        return ResponseEntity.ok(pedidoService.listarPedidosPorPeriodo(inicio, fim, paginacao));
    }
    
    @GetMapping("/previsao/pendente")
    public ResponseEntity<PedidoRespostaPaginaDTO> listarPendentesPorDataPrevisaoEntrega(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            Pageable paginacao
            ) 
    {     
        return ResponseEntity.ok(pedidoService.listarPrevisaoPedidosPendente(inicio, fim, paginacao));
    }
    
    @GetMapping("/entregues")
    public ResponseEntity<PedidoRespostaPaginaDTO> listarEntregues(
	        @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
	        @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
	        Pageable paginacao
	        )
    {
        return ResponseEntity.ok(pedidoService.listarEquipamentosEntregues(inicio, fim, paginacao));
    }

    @PatchMapping("/confirmar-equipamento")
    public ResponseEntity<EquipamentoRespostaDTO> confirmarEntregaEquipamento(@RequestBody @Valid PedidoConfirmarEquipamentoDTO dto) {
        return ResponseEntity.ok(pedidoService.confirmarEquipamento(dto));
    }

}
