package br.com.raroacademy.projetofinal.repository.equipamento;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;

public interface TipoEquipamentoRepository extends JpaRepository<TipoEquipamento, Long>{

	boolean existsByNomeIgnoreCase(String nome);

	Optional<TipoEquipamento> findByNomeIgnoreCase(String tipoEquipamento);
	
}
