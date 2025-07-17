package br.com.raroacademy.projetofinal.repository.parametro;

import br.com.raroacademy.projetofinal.model.parametro.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParametroRepositorio extends JpaRepository<Parametro, Long> {
    Optional<Parametro> findByTipoEquipamentoId(Long tipoEquipamentoId);
}
