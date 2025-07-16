package br.com.raroacademy.projetofinal.model.alocacao;

import java.time.LocalDate;

import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alocacao_equipamento")
@Getter
@Setter
public class AlocacaoEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "numero_serie", referencedColumnName = "numero_serie", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "colaborador_id", referencedColumnName = "id", nullable = false)
    private Colaborador colaborador;

    @Column(name = "data_entrega_prevista", nullable = false)
    private LocalDate dataEntregaPrevista;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(nullable = false)
    private boolean devolvido = false;
    
    @Enumerated(EnumType.STRING)
    private STATUS_EQUIPAMENTO statusEquipamento;

    private String observacao;
    
}