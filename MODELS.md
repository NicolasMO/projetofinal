# Models

## Usuário

Classe que representa os dados dos usuários do sistema da Raro, utilizando informações de nome, e-mail, e senha (encriptograda) associadas a um identificador do tipo `Long`.

```
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String email;
    private String senha;
}

```

## Equipamento

No diretório de ``Equipamento`` estão as classes com as informações das entidades associadas ao equipamento.

### Equipamento

A classe ``Equipamento``, com seu número de série do tipo `String` como chave primária, representa os dados de cada equipamento específico.

```
@Table(name = "equipamentos")
public class Equipamento {
	
	 @Id
	 @Column(name = "numero_serie", nullable = false, unique = true)
	 private String numeroSerie;
	 
	 @Column(nullable = false)
	 private String modelo;
	 
	 @Column(nullable = false)
	 private String marca;

	 @Column(name = "data_aquisicao")
	 private LocalDate dataAquisicao;

	 @Column(name = "tempo_uso")
	 private Integer tempoUso;

	 @Enumerated(EnumType.STRING)
	 private STATUS_EQUIPAMENTO status;
	 
	 @ManyToOne
	 @JoinColumn(name = "tipo_equipamento_id")
	 private TipoEquipamento tipoEquipamento;
	 
	 // Verificar depois como remover essa annotation JSON
	 @JsonManagedReference
	 @ManyToMany
	 @JoinTable(
	     name = "equipamentos_especificacoes",
	     joinColumns = @JoinColumn(name = "numero_serie"),
	     inverseJoinColumns = @JoinColumn(name = "especificacao_id")
	 )
	 private List<Especificacao> especificacoes;
}
```

### TipoEquipamento

A classe é utilizada para associar um `Equipamento` ao seu respectivo ``TipoEquipamento``, tendo campos como nome para identificar qual o tipo (celular, computador, etc.) e o tempo de configuração do tipo de equipamento.

```
@Table(name = "tipos_equipamentos")
public class TipoEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "tempo_configuracao_dias")
    private Integer tempoConfiguracao;
    
    public TipoEquipamento(String nome, Integer tempoConfiguracao) {
    	this.nome = nome;
    	this.tempoConfiguracao = tempoConfiguracao;
    }
}
```

### Especificacao

Classe implementada para representar uma ``Especificacao`` que um equipamento possa ter, identificando o que essa especificação é pela sua descrição, como Memória RAM (GB) ou Potência (W).

```
@Table(name = "especificacoes")
public class Especificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private String valor;
    
    // Verificar depois como remover essa anotation JSON
    @JsonBackReference
    @ManyToMany(mappedBy = "especificacoes")
    private List<Equipamento> equipamentos;
    
    // Construtor para cadastro    
    public Especificacao(String descricao, String valor) {
        this.descricao = descricao;
        this.valor = valor;
    }
}
```

- Página inicial: [HOME](https://git.raroacademy.com.br/nicolas.oliveira/projeto-final/-/tree/develop?ref_type=heads)