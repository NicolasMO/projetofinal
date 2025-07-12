package br.com.raroacademy.projetofinal.repository.usuario;
import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
