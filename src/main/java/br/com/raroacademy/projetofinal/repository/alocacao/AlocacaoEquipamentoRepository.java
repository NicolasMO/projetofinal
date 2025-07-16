package br.com.raroacademy.projetofinal.repository.alocacao;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoResumoProjection;
import br.com.raroacademy.projetofinal.model.alocacao.AlocacaoEquipamento;

@Repository
public interface AlocacaoEquipamentoRepository extends JpaRepository<AlocacaoEquipamento, Long> {
	
	@Query(value = """
			 		SELECT 
		            COUNT(*) AS totalPrevisto,
		            COUNT(CASE WHEN devolvido = TRUE THEN 1 END) AS totalEntregue,
		            COUNT(CASE WHEN devolvido = FALSE THEN 1 END) AS totalPendente
			        FROM alocacao_equipamento
			        WHERE data_devolucao_prevista BETWEEN :inicio AND :fim
			    	""", nativeQuery = true)
    AlocacaoEquipamentoResumoProjection buscarResumo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    Page<AlocacaoEquipamento> findByDataDevolucaoPrevistaBetween(LocalDate inicio, LocalDate fim, Pageable pageable);
    
    Page<AlocacaoEquipamento> findByColaboradorId(Long colaboradorId, Pageable pageable);

    Page<AlocacaoEquipamento> findByColaboradorIdAndDataDevolucaoPrevistaBetween(Long colaboradorId, LocalDate inicio, LocalDate fim, Pageable pageable);

}
