package br.com.raroacademy.projetofinal.controller.equipamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoAtualizarDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.service.equipamento.EquipamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {
	
	@Autowired
    private final EquipamentoService equipamentoService;

    @GetMapping("/{numeroSerie}")
    public ResponseEntity<EquipamentoRespostaDTO> buscarPorNumeroSerie(@PathVariable String numeroSerie) {
        return ResponseEntity.ok(equipamentoService.buscarPorNumeroSerie(numeroSerie));
    }
    
    @GetMapping
    public ResponseEntity<Page<EquipamentoRespostaDTO>> listarTodos(
    		@RequestParam(defaultValue = "0") int pagina,
    	    @RequestParam(defaultValue = "10") int tamanho,
    	    Pageable paginacao
    		) 
    {
        return ResponseEntity.ok(equipamentoService.listarTodos(paginacao));
    }
    
    @PostMapping
    public ResponseEntity<EquipamentoRespostaDTO> cadastrar(@Valid @RequestBody EquipamentoRequisicaoDTO dto) {
        return ResponseEntity.ok(equipamentoService.criar(dto));
    }
    
    @PutMapping("/{numeroSerie}")
    public ResponseEntity<EquipamentoRespostaDTO> atualizar(@PathVariable String numeroSerie, @Valid @RequestBody EquipamentoAtualizarDTO dto) {
        return ResponseEntity.ok(equipamentoService.atualizarEquipamento(numeroSerie, dto));
    }
    
    @DeleteMapping("/{numeroSerie}")
    public ResponseEntity<String> deletar(@PathVariable String numeroSerie) {
        equipamentoService.deletarEquipamento(numeroSerie);
        return ResponseEntity.ok("Equipamento excluído com sucesso!");
    }
    
}
