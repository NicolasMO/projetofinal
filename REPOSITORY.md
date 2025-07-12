# Repository

## Usuário

Encontra um usuário utilizando seu e-mail como parâmetro.

```
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}

```

## Equipamento

A entidade de ``Equipamento`` possui, além de seu próprio repositório, as implementações para o tipo do equipamento e para a especificação.

### EquipamentoRepository

A interface é implementada para buscar um ``Equipamento`` de acordo com seu número de série do tipo `String`.

```
public interface EquipamentoRepository extends JpaRepository<Equipamento, String>{}
```

### TipoEquipamentoRepository

A interface é implementada para buscar um ``TipoEquipamento`` de acordo com seu identificador do tipo `Long`.

```
public interface TipoEquipamentoRepository extends JpaRepository<TipoEquipamento, Long>{}
```

### EspecificacaoRepository

A interface é implementada para buscar uma ``Especificacao`` de acordo com seu identificador do tipo `Long`.

```
public interface EspecificacaoRepository  extends JpaRepository<Especificacao, Long>{}
```

- Página inicial: [HOME](https://git.raroacademy.com.br/nicolas.oliveira/projeto-final/-/tree/develop?ref_type=heads)