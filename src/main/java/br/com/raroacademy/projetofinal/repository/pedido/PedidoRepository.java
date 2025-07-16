package br.com.raroacademy.projetofinal.repository.pedido;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.pedido.EquipamentoPedido;
import br.com.raroacademy.projetofinal.model.pedido.Pedido;
import feign.Param;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
	@EntityGraph(attributePaths = {"equipamentos", "equipamentos.especificacoes"})
    Page<Pedido> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"equipamentos", "equipamentos.tipoEquipamento", "equipamentos.especificacoes"})
	Page<Pedido> findByDataSolicitacaoBetween(LocalDate inicio, LocalDate fim, Pageable pageable);
	
	@Query("SELECT DISTINCT p FROM Pedido p " +
		       "JOIN FETCH p.equipamentos eq " +
		       "JOIN FETCH eq.especificacoes espec " +
		       "WHERE p.dataPrevisaoEntrega BETWEEN :inicio AND :fim " +
		       "AND eq.entregue = false")
	Page<Pedido> findPedidosComEquipamentosPendentesNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim, Pageable paginacao);
	
	@Query("SELECT DISTINCT p FROM Pedido p " +
		       "JOIN FETCH p.equipamentos eq " +
		       "JOIN FETCH eq.especificacoes espec " +
		       "WHERE p.dataPrevisaoEntrega BETWEEN :inicio AND :fim " +
		       "AND eq.entregue = true")
	Page<Pedido> findPedidosComEquipamentosEntreguesNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim, Pageable paginacao);
	
	@Query("SELECT DISTINCT p FROM Pedido p " +
		       "JOIN FETCH p.equipamentos eq " +
		       "JOIN FETCH eq.tipoEquipamento " +
		       "JOIN FETCH eq.especificacoes " +
		       "WHERE p.dataPrevisaoEntrega BETWEEN :inicio AND :fim " +
		       "AND eq.entregue = false AND eq.tipoEquipamento = :tipo")
	Page<Pedido> findPedidosPorTipoEquipamentoNoPeriodo(@Param("tipo") TipoEquipamento tipo,
	                                             @Param("inicio") LocalDate inicio,
	                                             @Param("fim") LocalDate fim,
	                                             Pageable pageable);
	
	@Query("SELECT e FROM EquipamentoPedido e WHERE e.id = :id")
    Optional<EquipamentoPedido> buscarEquipamentoEntregaPorId(@Param("id") Long id);
	

	@Query("SELECT COUNT(eq) FROM EquipamentoPedido eq " +
	       "WHERE eq.pedido.dataPrevisaoEntrega BETWEEN :inicio AND :fim " +
	       "AND eq.tipoEquipamento = :tipo")
	long contarTotalPorTipo(@Param("tipo") TipoEquipamento tipo,
	                        @Param("inicio") LocalDate inicio,
	                        @Param("fim") LocalDate fim);

	@Query("SELECT COUNT(eq) FROM EquipamentoPedido eq " +
	       "WHERE eq.pedido.dataPrevisaoEntrega BETWEEN :inicio AND :fim " +
	       "AND eq.entregue = true AND eq.tipoEquipamento = :tipo")
	long contarEntreguesPorTipo(@Param("tipo") TipoEquipamento tipo,
	                            @Param("inicio") LocalDate inicio,
	                            @Param("fim") LocalDate fim);
	
	@Query("SELECT COUNT(equip) FROM EquipamentoPedido equip WHERE equip.pedido.dataSolicitacao BETWEEN :inicio AND :fim")
	long contarTotalEquipamentosSolicitadosNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
	
	@Query("SELECT COUNT(equip) FROM EquipamentoPedido equip WHERE equip.entregue = true AND equip.pedido.dataSolicitacao BETWEEN :inicio AND :fim")
	long contarTotalEquipamentosEntreguesNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
	
	@Query("SELECT COUNT(equip) FROM EquipamentoPedido equip WHERE equip.pedido.dataPrevisaoEntrega BETWEEN :inicio AND :fim")
	long contarEquipamentosComPrevisaoNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

	@Query("SELECT COUNT(equip) FROM EquipamentoPedido equip WHERE equip.entregue = true AND equip.pedido.dataPrevisaoEntrega BETWEEN :inicio AND :fim")
	long contarEquipamentosEntreguesComPrevisaoNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
	

	
}