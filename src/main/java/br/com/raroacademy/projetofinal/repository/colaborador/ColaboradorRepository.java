package br.com.raroacademy.projetofinal.repository.colaborador;

import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRespostaDTO;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    Page<Colaborador> findByStatusAtivoTrue(Pageable pageable);
    Page<Colaborador> findByStatusAtivoFalse(Pageable pageable);
}
