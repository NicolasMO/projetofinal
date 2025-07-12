package br.com.raroacademy.projetofinal.model.entregas;

import java.util.List;

import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "equipamento_entregas")
public class EquipamentoEntrega {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrega_id", nullable = false)
    private Entrega entrega;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false, length = 50)
    private String marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_equipamento_id", nullable = false)
    private TipoEquipamento tipoEquipamento;

    @OneToMany(mappedBy = "equipamentoEntrega", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipamentoEntregaEspecificacao> especificacoes;
    
    @Column(nullable = false)
    private boolean entregue = false;
    
}
