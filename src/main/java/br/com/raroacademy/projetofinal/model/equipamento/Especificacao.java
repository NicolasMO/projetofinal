package br.com.raroacademy.projetofinal.model.equipamento;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "especificacoes")
public class Especificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private String valor;
    
    @JsonBackReference
    @ManyToMany(mappedBy = "especificacoes")
    private List<Equipamento> equipamentos;
    
    public Especificacao(String descricao, String valor) {
        this.descricao = descricao;
        this.valor = valor;
    }
    
    public void atualizarDados(String descricao, String valor) {
    	this.descricao = descricao;
        this.valor = valor;
    }

}