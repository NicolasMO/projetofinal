package br.com.raroacademy.projetofinal.controller.usuario;

import br.com.raroacademy.projetofinal.controller.BaseController;
import br.com.raroacademy.projetofinal.dispachers.CommandBus;
import br.com.raroacademy.projetofinal.dto.usuario.*;
import br.com.raroacademy.projetofinal.model.ResponseModel;
import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.service.usuario.CreateUser.CreateUserCommand;
import br.com.raroacademy.projetofinal.service.usuario.UsuarioServico;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController extends BaseController {
    private final UsuarioServico usuarioServico;

    @Autowired
    public UsuarioController(
            UsuarioServico usuarioServico,
            CommandBus commandBus,
            HttpServletResponse httpServletResponse
    ) {
        super(commandBus, httpServletResponse);
        this.usuarioServico = usuarioServico;
    }

    @PostMapping
    public ResponseModel CreateUserCommand(@Valid @RequestBody CreateUserCommand command) {
        return ExecuteController(command, HttpStatus.CREATED.value(), HttpStatus.BAD_REQUEST.value());
    }

    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmarCadastro(@RequestParam String token) {
        usuarioServico.confirmarCadastro(token);
        return ResponseEntity.ok("Cadastro confirmado com sucesso.");
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@Valid @RequestBody EsqueciSenhaRequisicaoDTO dto) {
        usuarioServico.solicitarRecuperacaoSenha(dto.email());
        return ResponseEntity.ok("E-mail de recuperação enviado com sucesso.");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@Valid @RequestBody RedefinirSenhaDTO dto) {
        usuarioServico.redefinirSenha(dto.token(), dto.novaSenha());
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<UsuarioRespostaDTO>> listarTodos() {
        List<UsuarioRespostaDTO> usuarios = usuarioServico.listarTodos().stream().map(UsuarioRespostaDTO::new).collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioServico.buscarPorId(id);
        return ResponseEntity.ok(new UsuarioRespostaDTO(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequisicaoDTO dto) {
        Usuario atualizado = usuarioServico.atualizar(id, dto);
        return ResponseEntity.ok(new UsuarioRespostaDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        usuarioServico.deletar(id);
        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }

}
