package br.com.raroacademy.projetofinal.service.usuario.CreateUser;

import br.com.raroacademy.projetofinal.enums.TipoToken;
import br.com.raroacademy.projetofinal.exception.usuario.EmailJaCadastradoException;
import br.com.raroacademy.projetofinal.interfaces.ICommandHandler;
import br.com.raroacademy.projetofinal.model.usuario.TokenUsuario;
import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.repository.usuario.TokenUsuarioRepository;
import br.com.raroacademy.projetofinal.repository.usuario.UsuarioRepositorio;
import br.com.raroacademy.projetofinal.service.usuario.EmailServico;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CreateUserCommandHandler implements ICommandHandler<CreateUserCommand, Usuario> {
    private final UsuarioRepositorio usuarioRepositorio;
    private final TokenUsuarioRepository tokenUsuarioRepository;
    private final EmailServico emailServico;

    public CreateUserCommandHandler(
            UsuarioRepositorio usuarioRepositorio,
            TokenUsuarioRepository tokenUsuarioRepository,
            EmailServico emailServico
    ) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.tokenUsuarioRepository = tokenUsuarioRepository;
        this.emailServico = emailServico;
    }

    @Override
    public Usuario handle(CreateUserCommand command) {
        if (usuarioRepositorio.findByEmail(command.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException("Já existe um usuário com este e-mail.");
        }

        Usuario usuario = new Usuario().getUserModel(command);

        usuarioRepositorio.save(usuario);

        TokenUsuario token = setTokenUser(usuario);

        new Thread(() -> {
            SendMessageToUser(token, usuario.getEmail(), usuario.getNome());
        }).start();

        return usuario;
    }

    private TokenUsuario setTokenUser(Usuario usuario) {
        TokenUsuario token = new TokenUsuario(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusHours(2),
                TipoToken.CONFIRMACAO_CADASTRO,
                usuario
        );

        tokenUsuarioRepository.save(token);


        return token;
    }

    private void SendMessageToUser(TokenUsuario token, String email, String nome) {
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
                """, nome, link);

        emailServico.enviarEmail(email, "Ative sua conta", corpo);
    }
}
