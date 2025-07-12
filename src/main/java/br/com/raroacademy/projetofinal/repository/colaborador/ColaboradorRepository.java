package br.com.raroacademy.projetofinal.repository.colaborador;

import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRespostaDTO;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
   // @Query("""
     //            SELECT c
      //          FROM Colaborador c
      //         LEFT JOIN FETCH c.enderecos
      //      """)
   // List<Colaborador> buscarTodosColaboradores();

}
