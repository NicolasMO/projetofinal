package br.com.raroacademy.projetofinal.repository.entrega;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.raroacademy.projetofinal.model.entregas.Entrega;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
	
}