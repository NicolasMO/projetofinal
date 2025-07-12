# DTO

Para facilitar o manuseio de dados com o banco de dados, foi utilizado o padrão de projeto DTO (_DATA TRANSFER OBJECT_). No caso de ``Usuário``, é implementado para cuidar da requisição de login, enquanto para ``Equipamento`` cuida dos cadastros do mesmo, além de suas entidades associadas. Além disso, as classes são usadas como _Record_, já que possuem o propósito de apenas armazenar dados.

## Usuário

### UsuarioRequisicaoDTO

```
public record UsuarioRequisicaoDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha
) {}
```

### LoginRequisicaoDTO

```
public record LoginRequisicaoDTO(String email, String senha) {}
```

### LoginRespostaDTO

```
public record LoginRespostaDTO(String mensagem) {}
```

## Equipamento

### CadastroEquipamentoDTO

```
public record CadastroEquipamentoDTO(
		@NotNull @NotBlank
        String numeroSerie,
        
        @NotNull @NotBlank
        String modelo,
        
        @NotNull @NotBlank
        String marca,
        
        @NotNull
        LocalDate dataAquisicao,
        
        @NotNull
        @JsonDeserialize(using = DesserializadorLong.class)
        Long tipoEquipamentoId,
        
        @NotNull
        @JsonDeserialize(using = DesserializadorListaLong.class)
        List<Long> especificacoesIds
) {}
```

### TipoEquipamento

```
public record CadastroTipoEquipamentoDTO(
		@NotNull @NotBlank
        String nome,
        
        @NotNull
        @Min(value = 0, message = "O tempo de configuração deve ser 0 ou maior.") 
        Integer tempoConfiguracao
) {}
```

### Especificacao

```
public record CadastroEspecificacaoDTO(
        @NotNull @NotBlank(message = "A descrição é obrigatória")
        String descricao,
        
        @NotNull @NotBlank(message = "O valor é obrigatório")	
        String valor
) {}
```

- Página inicial: [HOME](https://git.raroacademy.com.br/nicolas.oliveira/projeto-final/-/tree/develop?ref_type=heads)