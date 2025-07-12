# Services

## Usuário

Para `Usuario` foram implementados dois controladores, um para as operações CRUD do próprio usuário e outro para a autenticação do mesmo.

### UsuarioController

Por meio dos métodos do serviço, faz o _mapping_ para realizar as operações, retornando um status HTTP.

```
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServico usuarioServico;

    @Autowired
    public UsuarioController(UsuarioServico usuarioServico) {
        this.usuarioServico = usuarioServico;
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioServico.criar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }
}
```

### AutenticacaoController

Realiza a verificação com os dados no banco, por meio do _repository_, das informações do usuário que está tentando realizar o _login_.

```
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginRespostaDTO> login(@RequestBody LoginRequisicaoDTO dadosLogin) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(dadosLogin.email());

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(401).body(new LoginRespostaDTO("Email ou senha inválidos"));
        }

        Usuario usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(dadosLogin.senha(), usuario.getSenha())) {
            return ResponseEntity.status(401).body(new LoginRespostaDTO("Email ou senha inválidos"));
        }

        return ResponseEntity.ok(new LoginRespostaDTO("Login realizado com sucesso"));
    }


}
```

## Equipamento

Todas as operações CRUD para `Equipamento` e as classes associadas.

### Equipamento

Utiliza o _mapping_ para realizar as operações para equipamento.

```
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<Equipamento> cadastrar(@Valid @RequestBody CadastroEquipamentoDTO dto) {
        Equipamento novo = equipamentoService.criar(dto);
        return ResponseEntity.ok(novo);
    }
}
```

### TipoEquipamento

Utiliza o _mapping_ para realizar as operações para o tipo do equipamento.

```
@RequestMapping("/tipo-equipamento")
public class TipoEquipamentoController {

	@Autowired
    private TipoEquipamentoService tipoEquipamentoService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody CadastroTipoEquipamentoDTO dto) {
        var novoTipoEquip = tipoEquipamentoService.criar(dto);
        return ResponseEntity.ok(novoTipoEquip);
    }
}
```

### Especificacao

Utiliza o _mapping_ para realizar as operações para as especificações do equipamento.

```
@RequestMapping("/especificacoes")
public class EspecificacaoController {

	@Autowired
    private EspecificacaoService especificacaoService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody CadastroEspecificacaoDTO dto) {
        var novaEspec = especificacaoService.criar(dto);
        return ResponseEntity.ok(novaEspec);
    }
}
```

- Página inicial: [HOME](https://git.raroacademy.com.br/nicolas.oliveira/projeto-final/-/tree/develop?ref_type=heads)