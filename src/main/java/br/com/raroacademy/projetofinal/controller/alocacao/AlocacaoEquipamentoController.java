package br.com.raroacademy.projetofinal.controller.alocacao;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRecebimentoDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRelatorioDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.service.alocacao.AlocacaoEquipamentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alocacoes/equipamentos")
public class AlocacaoEquipamentoController {

	@Autowired
    private AlocacaoEquipamentoService alocacaoEquipamentoService;

    @GetMapping
    public ResponseEntity<Page<AlocacaoEquipamentoRespostaDTO>> listarTodos(
		@RequestParam(defaultValue = "0") int pagina,
		@RequestParam(defaultValue = "10") int tamanho
		)
    {
    	Pageable paginacao = PageRequest.of(pagina, tamanho);
        Page<AlocacaoEquipamentoRespostaDTO> lista = alocacaoEquipamentoService.listarTodos(paginacao);
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/relatorio")
    public ResponseEntity<AlocacaoEquipamentoRelatorioDTO> gerarRelatorio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
    		) 
    {
    	Pageable paginacao = PageRequest.of(pagina, tamanho);
    	AlocacaoEquipamentoRelatorioDTO dto = alocacaoEquipamentoService.gerarRelatorio(inicio, fim, paginacao);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/colaborador/{id}")
    public ResponseEntity<AlocacaoEquipamentoRelatorioDTO> listarEquipamentosPorColaborador(
    		@PathVariable Long id,
    		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
    		@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
    		) 
    {
    	Pageable paginacao = PageRequest.of(pagina, tamanho);
    	AlocacaoEquipamentoRelatorioDTO dto = alocacaoEquipamentoService.buscarEquipamentosPorColaborador(id, inicio, fim, paginacao);
        return ResponseEntity.ok(dto);
    }

    
    @PutMapping("/confirmar-recebimento")
    public ResponseEntity<String> confirmarRecebimento(@RequestBody @Valid AlocacaoEquipamentoRecebimentoDTO dto) {
        alocacaoEquipamentoService.confirmarRecebimentoEquipamento(dto);
        return ResponseEntity.ok("Equipamento recebido com sucesso.");
    }
    
}

