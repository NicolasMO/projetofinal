package br.com.raroacademy.projetofinal.model.alocacao;

import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "alocacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alocacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "numero_serie")
    private Equipamento equipamento;

    @OneToOne
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;

    @Column(name = "data_envio")
    private LocalDate dataEnvio;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(name = "data_entrega_prevista")
    private LocalDate dataEntregaPrevista;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "data_devolucao_prevista")
    private LocalDate dataDevolucaoPrevista;

    public Alocacao(
            Equipamento equipamento,
            Colaborador colaborador,
            LocalDate dataEnvio,
            LocalDate dataEntrega,
            LocalDate dataEntregaPrevista,
            LocalDate dataDevolucao,
            LocalDate dataDevolucaoPrevista
    ) {
        this.equipamento = equipamento;
        this.colaborador = colaborador;
        this.dataEnvio = dataEnvio;
        this.dataEntrega = dataEntrega;
        this.dataEntregaPrevista = dataEntregaPrevista;
        this.dataDevolucao = dataDevolucao;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }
}
