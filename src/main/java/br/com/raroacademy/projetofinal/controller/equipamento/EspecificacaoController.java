package br.com.raroacademy.projetofinal.controller.equipamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.raroacademy.projetofinal.dto.equipamento.EspecificacaoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.service.equipamento.EspecificacaoService;
import jakarta.validation.Valid;
	
	@RestController
	@RequestMapping("/especificacoes")
	public class EspecificacaoController {
	
		@Autowired
	    private EspecificacaoService especificacaoService;
		
		@GetMapping("/{id}")
		public ResponseEntity<EspecificacaoRespostaDTO> buscarPorId(@PathVariable Long id) {
			return ResponseEntity.ok(especificacaoService.buscarPorId(id));
		}
		
		@GetMapping
		public ResponseEntity<Page<EspecificacaoRespostaDTO>> listarEspecificacoes(
	        @RequestParam(defaultValue = "0") int pagina,
	        @RequestParam(defaultValue = "10") int tamanho
			) 
		{
		    Pageable paginacao = PageRequest.of(pagina, tamanho);
		    Page<EspecificacaoRespostaDTO> paginaResultado = especificacaoService.listarTodos(paginacao);
		    return ResponseEntity.ok(paginaResultado);
		}
	
	    @PostMapping
	    public ResponseEntity<String> cadastrar(@Valid @RequestBody EspecificacaoRequisicaoDTO dto) {
	    	especificacaoService.criar(dto);
	        return ResponseEntity.ok("Especificação cadastrada com sucesso!");
	    }
	    
	    @PutMapping("/{id}")
	    public ResponseEntity<String> atualizar(@PathVariable Long id, @Valid @RequestBody EspecificacaoRequisicaoDTO dto) {
	        especificacaoService.atualizar(id, dto);
	        return ResponseEntity.ok("Especificação alterada com sucesso!");
	    }
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deletar(@PathVariable Long id) {
	        especificacaoService.deletar(id);
	        return ResponseEntity.ok("Especificação excluída com sucesso!");
	    }
	}