package br.com.raroacademy.projetofinal.model.pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "equipamento_pedidos_especificacoes")
public class EquipamentoPedidoEspecificacao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false, length = 100)
    private String valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_pedido_id", nullable = false)
    private EquipamentoPedido equipamentoPedido;
    
    public EquipamentoPedidoEspecificacao(String descricao, String valor, EquipamentoPedido equipamentoPedido) {
		this.descricao = descricao;
		this.valor = valor;
		this.equipamentoPedido = equipamentoPedido;
	}
}
