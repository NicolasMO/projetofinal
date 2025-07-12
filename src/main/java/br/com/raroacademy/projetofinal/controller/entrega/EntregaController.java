package br.com.raroacademy.projetofinal.controller.entrega;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.raroacademy.projetofinal.dto.entrega.EntregaRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.entrega.EntregaRespostaDTO;
import br.com.raroacademy.projetofinal.service.entrega.EntregaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @PostMapping
    public ResponseEntity<String> criarEntrega(@RequestBody @Valid EntregaRequisicaoDTO dto) {
        entregaService.solicitarEntrega(dto);
        return ResponseEntity.ok("Entrega solicitada com sucesso!");
    }
    
    @GetMapping
    public ResponseEntity<List<EntregaRespostaDTO>> listarEntregas() {
        List<EntregaRespostaDTO> entregas = entregaService.listarTodasEntregas();
        return ResponseEntity.ok(entregas);
    }
}
