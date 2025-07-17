package br.com.raroacademy.projetofinal.model.equipamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tipos_equipamentos")
public class TipoEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "tempo_configuracao_dias")
    private Integer tempoConfiguracao;
    
    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;
    
    public TipoEquipamento(String nome, Integer tempoConfiguracao, Integer estoqueMinimo) {
    	this.nome = nome;
    	this.tempoConfiguracao = tempoConfiguracao;
    	this.estoqueMinimo = estoqueMinimo;
    }
    
    public void atualizarDados(String nome, Integer tempoConfiguracao, Integer estoqueMinimo) {
        this.nome = nome;
        this.tempoConfiguracao = tempoConfiguracao;
        this.estoqueMinimo = estoqueMinimo;
    }

}