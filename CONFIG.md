# Configuration

## Usuário

Para `Usuario` são utilizadas duas classes de configurações. A primeira garante segurança nas requisições HTTP, enquanto a segunda cuida da criptografia da senha do usuário.

### ConfiguracaoSeguranca

```
@Configuration
public class ConfiguracaoSeguranca {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
```

### ConfiguracaoBeansSeguranca

```
@Configuration
public class ConfiguracaoBeansSeguranca {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## Desserializador

Classes que tratam os números de série dos equipamentos. A primeira é utilizada para um número, enquanto a segunda trata uma lista de números de série.

### DesserializadorLong

```
public class DesserializadorLong extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    	
        if (parser.isExpectedNumberIntToken()) {
            return parser.getLongValue();
        }

        throw MismatchedInputException.from(parser, Long.class, "Esperado valor inteiro para campo Long");
    }
}
```

### DesserializadorListaLong

```
public class DesserializadorListaLong extends JsonDeserializer<List<Long>> {

    @Override
    public List<Long> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<Long> result = new ArrayList<>();

        if (parser.currentToken() != JsonToken.START_ARRAY) {
            throw MismatchedInputException.from(parser, List.class, "Esperado um array de inteiros");
        }

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.currentToken().isNumeric()) {
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    result.add(parser.getLongValue());
                } else {
                    throw MismatchedInputException.from(parser, Long.class, "Valor decimal não permitido em especificacoesIds");
                }
            } else {
                throw MismatchedInputException.from(parser, Long.class, "Esperado valor numérico na lista especificacoesIds");
            }
        }

        return result;
    }	
}
```

- Página inicial: [HOME](https://git.raroacademy.com.br/nicolas.oliveira/projeto-final/-/tree/develop?ref_type=heads)