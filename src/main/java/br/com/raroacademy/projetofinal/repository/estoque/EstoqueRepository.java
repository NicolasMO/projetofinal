package br.com.raroacademy.projetofinal.repository.estoque;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.raroacademy.projetofinal.dto.estoque.EstoqueGeralRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;

public interface EstoqueRepository extends JpaRepository<Equipamento, String>{
	@Query("""
	        SELECT new br.com.raroacademy.projetofinal.dto.estoque.EstoqueGeralRespostaDTO(
			    t.id,
	            t.nome,
	            t.estoqueMinimo,
	            COUNT(e)
	        )
	        FROM Equipamento e
	        JOIN e.tipoEquipamento t
	        WHERE (:tipoEquipamento IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', CAST(:tipoEquipamento AS string), '%')))
			    AND (:status IS NULL OR e.status IN :status)
	        GROUP BY t.id, t.nome, t.estoqueMinimo
	        ORDER BY t.nome
	    """)
	    Page<EstoqueGeralRespostaDTO> listarEstoquePorTipoEStatus(
	    		@Param("tipoEquipamento") String tipoEquipamento,
	    		@Param("status") List<STATUS_EQUIPAMENTO> status,
	    		Pageable paginacao
		);
	
	@Query("""
	        SELECT e FROM Equipamento e
	        JOIN FETCH e.tipoEquipamento t
	        LEFT JOIN FETCH e.especificacoes
	        WHERE (:tipoEquipamento IS NULL OR LOWER(t.nome) = LOWER(CAST(:tipoEquipamento AS string)))
	          AND (:status IS NULL OR e.status IN :status)
	    """)
	    Page<Equipamento> buscarEquipamentosDetalhado(
	        @Param("tipoEquipamento") String tipoEquipamento,
	        @Param("status") List<STATUS_EQUIPAMENTO> status,
	        Pageable paginacao
	    );
	
	Long countByTipoEquipamentoId(Long tipoEquipamentoId);
	Long countByTipoEquipamentoIdAndStatusIn(Long tipoEquipamentoId, List<STATUS_EQUIPAMENTO> status);

}
