package br.com.raroacademy.projetofinal.model.colaborador;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colaborador")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(length = 150, nullable = false)
    private String cargo;

    @Column(length = 15, nullable = false)
    private String telefone;

    @Column(name = "data_admissao",nullable = false)
    private Date data_admissao;

    @Column(name = "data_demissao")
    private Date data_demissao;

    @OneToOne(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    private EnderecoColaborador endereco;
}

