package br.com.raroacademy.projetofinal.model.entregas;

import java.time.LocalDate;
import java.util.List;

import br.com.raroacademy.projetofinal.enums.STATUS_ENTREGA;
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
@Table(name = "entregas")
public class Entrega {
	
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
    private STATUS_ENTREGA status;

    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipamentoEntrega> equipamentos;

}
