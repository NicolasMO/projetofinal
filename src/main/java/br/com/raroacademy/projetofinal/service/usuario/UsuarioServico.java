package br.com.raroacademy.projetofinal.service.usuario;

import br.com.raroacademy.projetofinal.dto.usuario.UsuarioRequisicaoDTO;
import br.com.raroacademy.projetofinal.enums.TipoToken;
import br.com.raroacademy.projetofinal.exception.usuario.*;
import br.com.raroacademy.projetofinal.model.usuario.TokenUsuario;
import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.repository.usuario.TokenUsuarioRepository;
import br.com.raroacademy.projetofinal.repository.usuario.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServico {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder codificador;
    private final TokenUsuarioRepository tokenUsuarioRepository;
    private final EmailServico emailServico;

    @Autowired
    public UsuarioServico(
            UsuarioRepositorio usuarioRepositorio,
            PasswordEncoder codificador,
            TokenUsuarioRepository tokenUsuarioRepository,
            EmailServico emailServico
    ) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.codificador = codificador;
        this.tokenUsuarioRepository = tokenUsuarioRepository;
        this.emailServico = emailServico;
    }

    public Usuario criarDTO(UsuarioRequisicaoDTO dto) {
        if (usuarioRepositorio.findByEmail(dto.email()).isPresent()) {
            throw new EmailJaCadastradoException("Já existe um usuário com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email().toLowerCase());
        usuario.setSenha(codificador.encode(dto.senha()));
        usuario.setAtivo(false);

        usuarioRepositorio.save(usuario);

        TokenUsuario token = new TokenUsuario(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusHours(2),
                TipoToken.CONFIRMACAO_CADASTRO,
                usuario
        );
        tokenUsuarioRepository.save(token);

        String link = "http://localhost:8080/usuarios/confirmar?token=" + token.getToken();
        String corpo = String.format("""
            <html>
            <body>
                <h2>Confirmação de Cadastro</h2>
                <p>Olá, %s!</p>
                <p>Confirme seu cadastro clicando no link abaixo:</p>
                <p><a href="%s">Confirmar Cadastro</a></p>
                <p><em>Este link expira em 2 horas.</em></p>
            </body>
            </html>
        """, usuario.getNome(), link);

        emailServico.enviarEmail(usuario.getEmail(), "Ative sua conta", corpo);

        return usuario;
    }

    public void confirmarCadastro(String token) {
        TokenUsuario tokenUsuario = tokenUsuarioRepository.findByToken(token).orElseThrow(() -> new TokenInvalidoException("Token invalido."));

        if (tokenUsuario.isUtilizado() || tokenUsuario.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new TokenExpiradoOuUtilizadoException("Token expirado ou já utilizado.");
        }

        if (!tokenUsuario.getTipo().equals(TipoToken.CONFIRMACAO_CADASTRO)) {
            throw new RuntimeException("Tipo de token inválido para confirmação de cadastro.");
        }

        Usuario usuario = tokenUsuario.getUsuario();
        usuario.setAtivo(true);
        usuarioRepositorio.save(usuario);

        tokenUsuario.setUtilizado(true);
        tokenUsuarioRepository.save(tokenUsuario);
    }

    public void solicitarRecuperacaoSenha(String email) {
        String emailNormalizado = email.toLowerCase();

        Usuario usuario = usuarioRepositorio.findByEmail(emailNormalizado).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com este e-mail não foi encontrado."));

        TokenUsuario token = new TokenUsuario(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusHours(2),
                TipoToken.RECUPERACAO_SENHA,
                usuario
        );
        tokenUsuarioRepository.save(token);

        String corpo = String.format("""
            <html>
            <body>
                <h2>Recuperação de Senha</h2>
                <p>Olá, %s!</p>
                <p>Use o código abaixo para redefinir sua senha:</p>
                <p style="font-size: 14px; font-weight: bold;">%s</p>
                <p><em>Este código é válido por 2 horas.</em></p>
            </body>
            </html>
        """, usuario.getNome(), token.getToken());

        emailServico.enviarEmail(usuario.getEmail(), "Recupere sua senha", corpo);
    }

    public void redefinirSenha(String token, String novaSenha) {
        TokenUsuario tokenRecuperacao = tokenUsuarioRepository.findByToken(token).orElseThrow(() -> new TokenInvalidoException("Este token não é válido."));

        if (tokenRecuperacao.isUtilizado() || tokenRecuperacao.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new TokenExpiradoOuUtilizadoException("Este token está expirado ou já foi utilizado.");
        }

        if (!tokenRecuperacao.getTipo().equals(TipoToken.RECUPERACAO_SENHA)) {
            throw new RuntimeException("Tipo de token inválido para redefinição de senha.");
        }

        Usuario usuario = tokenRecuperacao.getUsuario();

        if (codificador.matches(novaSenha, usuario.getSenha())) {
            throw new SenhaRepetidaException("A nova senha não pode ser igual à senha atual.");
        }

        usuario.setSenha(codificador.encode(novaSenha));
        usuarioRepositorio.save(usuario);

        tokenRecuperacao.setUtilizado(true);
        tokenUsuarioRepository.save(tokenRecuperacao);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    public Usuario atualizar(Long id, UsuarioRequisicaoDTO dto) {
        Usuario usuario = usuarioRepositorio.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email().toLowerCase());
        usuario.setSenha(codificador.encode(dto.senha()));

        return usuarioRepositorio.save(usuario);
    }

    public void deletar(Long id) {
        if (!usuarioRepositorio.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }
        usuarioRepositorio.deleteById(id);
    }
    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
    }
}
