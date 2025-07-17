package br.com.raroacademy.projetofinal.repository.alocacao;

import br.com.raroacademy.projetofinal.model.alocacao.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {
    @Query("""
        SELECT m FROM Alocacao m 
        WHERE m.equipamento.tipoEquipamento.id = :tipoEquipamentoId
        AND m.dataEnvio IS NOT NULL
        ORDER BY m.dataEnvio ASC
    """)
    List<Alocacao> buscarTodosEnviosPorTipo(Long tipoEquipamentoId);
}
