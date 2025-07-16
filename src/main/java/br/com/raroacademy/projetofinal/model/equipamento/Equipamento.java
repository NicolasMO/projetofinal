package br.com.raroacademy.projetofinal.model.equipamento;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "equipamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Equipamento {
	
	 @Id
	 @Column(name = "numero_serie", nullable = false, unique = true)
	 private String numeroSerie;
	 
	 @Column(nullable = false)
	 private String modelo;
	 
	 @Column(nullable = false)
	 private String marca;

	 @Column(name = "data_aquisicao")
	 private LocalDate dataAquisicao;

	 @Column(name = "tempo_uso")
	 private Integer tempoUso;

	 @Enumerated(EnumType.STRING)
	 private STATUS_EQUIPAMENTO status;
	 
	 @ManyToOne
	 @JoinColumn(name = "tipo_equipamento_id")
	 private TipoEquipamento tipoEquipamento;
	 
	 // Verificar depois como remover essa annotation JSON
	 @JsonManagedReference
	 @ManyToMany
	 @JoinTable(
	     name = "equipamentos_especificacoes",
	     joinColumns = @JoinColumn(name = "numero_serie"),
	     inverseJoinColumns = @JoinColumn(name = "especificacao_id")
	 )
	 private Set<Especificacao> especificacoes;
}
