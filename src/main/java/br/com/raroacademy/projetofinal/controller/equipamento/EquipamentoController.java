package br.com.raroacademy.projetofinal.controller.equipamento;

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

import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoAtualizarDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.service.equipamento.EquipamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @GetMapping("/{numeroSerie}")
    public ResponseEntity<EquipamentoRespostaDTO> buscarPorNumeroSerie(@PathVariable String numeroSerie) {
        return ResponseEntity.ok(equipamentoService.buscarPorNumeroSerie(numeroSerie));
    }
    
    @GetMapping
    public ResponseEntity<Page<EquipamentoRespostaDTO>> listarPaginado(
    		@RequestParam(defaultValue = "0") int pagina,
    	    @RequestParam(defaultValue = "10") int tamanho
    		) 
    {
    	Pageable paginacao = PageRequest.of(pagina, tamanho);
        Page<EquipamentoRespostaDTO> paginaResultado = equipamentoService.listarTodos(paginacao);
        return ResponseEntity.ok(paginaResultado);
    }
    
    @PostMapping
    public ResponseEntity<String> cadastrar(@Valid @RequestBody EquipamentoRequisicaoDTO dto) {
        equipamentoService.criar(dto);
        return ResponseEntity.ok("Equipamento cadastrado com sucesso!");
    }
    
    @PutMapping("/{numeroSerie}")
    public ResponseEntity<String> atualizar(@PathVariable String numeroSerie, @Valid @RequestBody EquipamentoAtualizarDTO dto) {
        equipamentoService.atualizar(numeroSerie, dto);
        return ResponseEntity.ok("Equipamento alterado com sucesso!");
    }
    
    @DeleteMapping("/{numeroSerie}")
    public ResponseEntity<String> deletar(@PathVariable String numeroSerie) {
        equipamentoService.deletar(numeroSerie);
        return ResponseEntity.ok("Equipamento excluído com sucesso!");
    }
}
