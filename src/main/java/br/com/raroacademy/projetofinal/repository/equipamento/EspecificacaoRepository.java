package br.com.raroacademy.projetofinal.repository.equipamento;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;

public interface EspecificacaoRepository extends JpaRepository<Especificacao, Long> {
	
	boolean existsByDescricaoAndValor(String descricao, String valor);

	Optional<Especificacao> findByDescricaoIgnoreCaseAndValorIgnoreCase(String descricao, String valor);
}