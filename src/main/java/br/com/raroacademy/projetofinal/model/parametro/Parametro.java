package br.com.raroacademy.projetofinal.model.parametro;

import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "parametros")
@Getter @Setter
@NoArgsConstructor
public class Parametro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tempo_medio_reposicao_dias", nullable = false)
    private Integer tempoMedioReposicaoDias;

    @Column(name = "tempo_medio_consumo_estoque_dias", nullable = false)
    private Integer tempoMedioConsumoEstoqueDias;

    @Column(name = "taxa_media_defeituosos_percentual", nullable = false)
    private Double taxaMediaDefeituososPercentual;

    @OneToMany(mappedBy = "parametro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TempoEnvioPorRegiao> temposEnvio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_equipamento_id")
    private TipoEquipamento tipoEquipamento;
}
