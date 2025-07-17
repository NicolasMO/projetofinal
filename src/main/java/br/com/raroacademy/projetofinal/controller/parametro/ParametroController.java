package br.com.raroacademy.projetofinal.controller.parametro;

import br.com.raroacademy.projetofinal.dto.parametro.ParametroDTO;
import br.com.raroacademy.projetofinal.model.parametro.Parametro;
import br.com.raroacademy.projetofinal.service.parametro.ParametroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

    @Autowired
    private ParametroService service;

    @PostMapping
    public ResponseEntity<String> criar(@Valid @RequestBody ParametroDTO dto) {
        service.criar(dto);
        return ResponseEntity.ok("Parâmetro criado com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<Parametro>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @Valid @RequestBody ParametroDTO dto) {
        service.atualizar(id, dto);
        return ResponseEntity.ok("Parâmetro atualizado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok("Parâmetro removido com sucesso.");
    }
}
