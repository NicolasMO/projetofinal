package br.com.raroacademy.projetofinal.controller.colaborador;

import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRespostaDTO;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import br.com.raroacademy.projetofinal.service.colaborador.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaborador")
public class ColaboradorController {

    @Autowired
    private ColaboradorService service;

    @PostMapping
    public void criar(@RequestBody ColaboradorRequisicaoDTO colaboradorRequisicaoDTO){
        service.salvarColaborador(colaboradorRequisicaoDTO);
    }

    @GetMapping("/{id}")
    public ColaboradorRespostaDTO buscaPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<Colaborador> buscarTodosColaboradores(){
        return service.buscarTodosColaboradores();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPorId(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.ok("Colaborador excluído com sucesso!");
    }

}
