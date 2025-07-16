package br.com.raroacademy.projetofinal.controller.colaborador;


import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorAtualizacaoParcialDTO;
import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRespostaDTO;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import br.com.raroacademy.projetofinal.service.colaborador.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/colaborador")
public class ColaboradorController {

    @Autowired
    private ColaboradorService service;

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid ColaboradorRequisicaoDTO dto) {
        service.salvarColaborador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("mensagem", "Colaborador criado com sucesso!")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscaPorId(@PathVariable Long id) {
        ColaboradorRespostaDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> buscarTodosColaboradores() {
        Page<ColaboradorRespostaDTO> pagina = service.buscarTodosColaboradoresPaginados();
        return ResponseEntity.ok(
                Map.of("mensagem", "Colaboradores encontrados com sucesso!", "dados", pagina)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletarPorId(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador excluído com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id, @RequestBody @Valid ColaboradorRequisicaoDTO dto) {
        service.atualizarPorId(id, dto);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador atualizado com sucesso!"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizarParcial(@PathVariable Long id, @Valid @RequestBody ColaboradorAtualizacaoParcialDTO dto) {
        service.atualizarParcial(id, dto);
        return ResponseEntity.ok(Map.of("mensagem", "Atualização parcial concluída com sucesso!"));
    }

    @GetMapping("/inativos")
    public ResponseEntity<Map<String, Object>> listarInativos() {
        Page<ColaboradorRespostaDTO> pagina = service.buscarColaboradoresInativos();
        return ResponseEntity.ok(
                Map.of("mensagem", "Colaboradores inativos encontrados com sucesso!", "dados", pagina)
        );
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Long id) {
        service.desativarColaborador(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador desativado com sucesso!"));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Map<String, String>> ativar(@PathVariable Long id) {
        service.ativarColaborador(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador reativado com sucesso!"));
    }
}