package br.com.raroacademy.projetofinal.controller.usuario;

import br.com.raroacademy.projetofinal.dto.usuario.LoginRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.usuario.LoginRespostaDTO;
import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.repository.usuario.UsuarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final UsuarioRepositorio usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoController(UsuarioRepositorio usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespostaDTO> login(@RequestBody LoginRequisicaoDTO dadosLogin) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(dadosLogin.email());

        if (usuarioOptional.isEmpty() ||
                !passwordEncoder.matches(dadosLogin.senha(), usuarioOptional.get().getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginRespostaDTO("Email ou senha inválidos"));
        }

        Usuario usuario = usuarioOptional.get();

        if (!usuario.isAtivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginRespostaDTO("Conta ainda não confirmada."));
        }

        return ResponseEntity.ok(new LoginRespostaDTO("Login realizado com sucesso"));
    }
}
