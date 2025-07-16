package br.com.raroacademy.projetofinal.config.usuario;

import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.repository.usuario.UsuarioRepositorio;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ServicoDetalhesDoUsuario implements UserDetailsService {

    private final UsuarioRepositorio usuarios;

    public ServicoDetalhesDoUsuario(UsuarioRepositorio usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarios.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário com e-mail '" + email + "' não encontrado."));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USUARIO")
                .build();
    }
}
