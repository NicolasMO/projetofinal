package br.com.raroacademy.projetofinal.model.pedido;

import java.util.HashSet;
import java.util.Set;

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
@Table(name = "equipamento_pedidos")
public class EquipamentoPedido {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false, length = 50)
    private String marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_equipamento_id", nullable = false)
    private TipoEquipamento tipoEquipamento;

    @OneToMany(mappedBy = "equipamentoPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EquipamentoPedidoEspecificacao> especificacoes = new HashSet<>()															;
    
    @Column(nullable = false)
    private boolean entregue = false;
    
    public EquipamentoPedido(Pedido pedido, String marca, String modelo, TipoEquipamento tipoEquipamento) {
		this.pedido = pedido;
		this.marca = marca;
		this.modelo = modelo;
		this.tipoEquipamento = tipoEquipamento;
	}

}
