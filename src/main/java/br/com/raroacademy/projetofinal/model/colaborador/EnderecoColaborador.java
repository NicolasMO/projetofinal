package br.com.raroacademy.projetofinal.model.colaborador;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "endereco_colaborador")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoColaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_endereco", length = 50)
    private String tipoEndereco;

    @Column(length = 255)
    private String logradouro;

    @Column(length = 255)
    private String bairro;

    @Column(length = 2)
    private String uf;

    @Column(length = 50)
    private String estado;

    @Column(length = 20)
    private String cep;

    @Column(length = 30)
    private String regiao;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;
}

