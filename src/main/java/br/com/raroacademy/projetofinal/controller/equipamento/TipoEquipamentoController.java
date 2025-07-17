package br.com.raroacademy.projetofinal.controller.equipamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento.AtualizarTipoEquipamentoEstoqueMinimoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento.TipoEquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento.TipoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.service.equipamento.TipoEquipamentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tipos-equipamentos")
public class TipoEquipamentoController {

	@Autowired
    private TipoEquipamentoService tipoEquipamentoService;
	
	@GetMapping("/{id}")
    public ResponseEntity<TipoEquipamentoRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoEquipamentoService.buscarPorId(id));
    }
	
	@GetMapping
    public ResponseEntity<Page<TipoEquipamentoRespostaDTO>> listarTodos(
	        @RequestParam(defaultValue = "0") int pagina,
	        @RequestParam(defaultValue = "10") int tamanho,
	        Pageable paginacao
			) 
	{
        return ResponseEntity.ok(tipoEquipamentoService.listarTodos(paginacao));
    }

    @PostMapping
    public ResponseEntity<TipoEquipamentoRespostaDTO> cadastrar(@Valid @RequestBody TipoEquipamentoRequisicaoDTO dto) {
        return ResponseEntity.ok(tipoEquipamentoService.criar(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TipoEquipamentoRespostaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TipoEquipamentoRequisicaoDTO dto) {
        return ResponseEntity.ok(tipoEquipamentoService.atualizarTipoEquipamento(id, dto));
    }
    
    @PatchMapping("/{id}/estoque-minimo")
    public ResponseEntity<TipoEquipamentoRespostaDTO> atualizarEstoqueMinimo(@PathVariable Long id, @Valid @RequestBody AtualizarTipoEquipamentoEstoqueMinimoDTO dto) {
        return ResponseEntity.ok(tipoEquipamentoService.atualizarEstoqueMinimo(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        tipoEquipamentoService.deletar(id);
        return ResponseEntity.ok("Tipo de Equipamento excluído com sucesso!");
    }
    
}