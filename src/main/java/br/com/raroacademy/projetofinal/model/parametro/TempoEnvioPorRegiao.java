package br.com.raroacademy.projetofinal.model.parametro;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tempos_envio_por_regiao")
@Getter @Setter
@NoArgsConstructor
public class TempoEnvioPorRegiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String regiao;

    @Column(name = "tempo_envio_dias", nullable = false)
    private Integer tempoEnvioDias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parametro_id", nullable = false)
    @JsonBackReference
    private Parametro parametro;
}
