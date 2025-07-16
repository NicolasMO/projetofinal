package br.com.raroacademy.projetofinal.model.pedido;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import br.com.raroacademy.projetofinal.enums.STATUS_PEDIDO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pedidos")
public class Pedido {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(name = "data_previsao_entrega", nullable = false)
    private LocalDate dataPrevisaoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private STATUS_PEDIDO status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EquipamentoPedido> equipamentos = new HashSet<>();

}
