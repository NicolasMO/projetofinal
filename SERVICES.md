# Services

## Usuário

Classe de serviço do `Usuario` que realiza as operações de CRUD, implementando um _encoder_ para criptografar a senha.

```
@Service
public class UsuarioServico {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder encoder;

    @Autowired
    public UsuarioServico(UsuarioRepositorio usuarioRepositorio, PasswordEncoder encoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.encoder = encoder;
    }

    public Usuario criar(Usuario usuario) {

        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return usuarioRepositorio.save(usuario);
    }
}


```

## Equipamento

No diretório de ``Equipamento`` estão os serviços para as classes associadas ao equipamento.

### Equipamento

O serviço de ``Equipamento`` realiza as operações de CRUD juntamente dos repositórios de `TipoEquipamento` e `Especificacao` para ter a relação entre um equipamento com seu tipo e especificações.

```
@Service
public class EquipamentoService {

	@Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;

    @Autowired
    private EspecificacaoRepository especificacaoRepository;


    public Equipamento criar(CadastroEquipamentoDTO dto) {
    	
    	// Verifica se já existe aquele número de série cadastrado
    	if (equipamentoRepository.existsById(dto.numeroSerie())) {
    		throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um equipamento com esse número de série");
        }
    	
    	// buscar o tipo de equipamento
        TipoEquipamento tipo = tipoEquipamentoRepository.findById(dto.tipoEquipamentoId())
            .orElseThrow(() -> new IllegalArgumentException("Tipo de equipamento não encontrado"));

        // buscar as especificacoes
        List<Especificacao> especificacoes = especificacaoRepository.findAllById(dto.especificacoesIds());

        // criar o objeto
        Equipamento novoEquipamento = new Equipamento(
                dto.numeroSerie(),
                dto.modelo(),
                dto.marca(),
                dto.dataAquisicao(),
                0, // tempo de uso sempre zero para produtos novos
                STATUS_EQUIPAMENTO.DISPONIVEL, // status inicial
                tipo,
                especificacoes
        );

        return equipamentoRepository.save(novoEquipamento);
    }
}

```

### TipoEquipamento

Serviço para realizar operações de CRUD para os tipos de `Equipamento`.

```
@Service
public class TipoEquipamentoService {

    @Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;

    public TipoEquipamento criar(CadastroTipoEquipamentoDTO dto) {
        TipoEquipamento tipo = new TipoEquipamento(dto.nome(), dto.tempoConfiguracao());
        return tipoEquipamentoRepository.save(tipo);
    }
}
```

### Especificacao

Assim como as outras, faz operações CRUD, dessa vez as especificações que podem estar associadas a um `Equipamento`.

```
@Service
public class EspecificacaoService {

	@Autowired
	private EspecificacaoRepository especificacaoReposity;
	
	public Especificacao criar(@Valid @RequestBody CadastroEspecificacaoDTO dto) {
		Especificacao espec = new Especificacao(dto.descricao(), dto.valor());
		
		return especificacaoReposity.save(espec);
	}
}
```

- Página inicial: [HOME](https://git.raroacademy.com.br/nicolas.oliveira/projeto-final/-/tree/develop?ref_type=heads)