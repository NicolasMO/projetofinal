package br.com.raroacademy.projetofinal.controller.alocacao;

import br.com.raroacademy.projetofinal.dto.alocacao.*;
import br.com.raroacademy.projetofinal.service.alocacao.AlocacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alocacao")
@RequiredArgsConstructor
public class AlocacaoController {

    private final AlocacaoService alocacaoService;

    @GetMapping
    public ResponseEntity<Page<AlocacaoRespostaDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
    ) {
        Pageable paginacao = PageRequest.of(pagina, tamanho);
        Page<AlocacaoRespostaDTO> paginaResultado = alocacaoService.listarTodas(paginacao);
        return ResponseEntity.ok(paginaResultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlocacaoRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alocacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@Valid @RequestBody AlocacaoRequisicaoDTO dto) {
        alocacaoService.cadastrar(dto);
        return ResponseEntity.ok("Alocação cadastrada com sucesso");
    }

    @PutMapping("/{id}/data-envio")
    public ResponseEntity<String> atualizarDataEnvio(@PathVariable Long id, @Valid @RequestBody AlocacaoAtualizarDatasDTO dto) {
        alocacaoService.atualizaDataEnvio(id, dto);
        return ResponseEntity.ok("Data de envio alterada com sucesso");
    }

    @PutMapping("/{id}/data-entrega")
    public ResponseEntity<String> atualizarDataEntrega(@PathVariable Long id, @Valid @RequestBody AlocacaoAtualizarDatasDTO dto) {
        alocacaoService.atualizaDataEntrega(id, dto);
        return ResponseEntity.ok("Data de entrega alterada com sucesso");
    }

    @PutMapping("/{id}/data-devolucao")
    public ResponseEntity<String> atualizarDataDevolucao(@PathVariable Long id, @Valid @RequestBody AlocacaoAtualizarDatasDTO dto) {
        alocacaoService.atualizaDataDevolucao(id, dto);
        return ResponseEntity.ok("Data de devolução alterada com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        alocacaoService.deletar(id);
        return ResponseEntity.ok("Alocação excluída com sucesso");
    }
}
