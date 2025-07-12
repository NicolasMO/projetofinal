package br.com.raroacademy.projetofinal.repository.usuario;

import br.com.raroacademy.projetofinal.model.usuario.TokenUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenUsuarioRepository extends JpaRepository<TokenUsuario, Long> {
    Optional<TokenUsuario> findByToken(String token);
}
