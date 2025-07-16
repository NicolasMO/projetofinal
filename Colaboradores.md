# Colaboradores

Em colaborador foi feito um crud completo para controlar os dados do colaborador, você pode
criar, alterar, buscar e deletar o usuario.

### ColaboradoresControllers

No controller tem os seguintes caminhos para fazer o crud :

```
@PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid ColaboradorRequisicaoDTO dto) {
        service.salvarColaborador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("mensagem", "Colaborador criado com sucesso!")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscaPorId(@PathVariable Long id) {
        ColaboradorRespostaDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> buscarTodosColaboradores() {
        Page<ColaboradorRespostaDTO> pagina = service.buscarTodosColaboradoresPaginados();
        return ResponseEntity.ok(
                Map.of("mensagem", "Colaboradores encontrados com sucesso!", "dados", pagina)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletarPorId(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador excluído com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id, @RequestBody @Valid ColaboradorRequisicaoDTO dto) {
        service.atualizarPorId(id, dto);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador atualizado com sucesso!"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizarParcial(@PathVariable Long id, @Valid @RequestBody ColaboradorAtualizacaoParcialDTO dto) {
        service.atualizarParcial(id, dto);
        return ResponseEntity.ok(Map.of("mensagem", "Atualização parcial concluída com sucesso!"));
    }
        
```
Para inserir dados via Json de acordo o crud, se utiliza os seguintes dados:

``` 
    {
  "nome": "Colaborador joe",
  "email": "joebot@qualquer.com",
  "cpf": "37044033008",
  "cargo": "Desenvolvedor java junior",
  "telefone": "(61) 99999-9999",
  "data_admissao": "2023-11-10",
  "data_demissao": "2024-11-10",
  "endereco": 
    {
      "cep": "30514-100",
      "tipo_endereco": "residencial"

    }
  
}

```
É possivel tambem acessar dados dos colaboradores inativos pelos endpoints,
é possivel tambem, deixar um colaborador desativado, ou reativar um colaborador
desativado, utilizando somente o {id} do cadastro na URL.

```
        @GetMapping("/inativos")
    public ResponseEntity<Map<String, Object>> listarInativos() {
        Page<ColaboradorRespostaDTO> pagina = service.buscarColaboradoresInativos();
        return ResponseEntity.ok(
                Map.of("mensagem", "Colaboradores inativos encontrados com sucesso!", "dados", pagina)
        );
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Long id) {
        service.desativarColaborador(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador desativado com sucesso!"));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Map<String, String>> ativar(@PathVariable Long id) {
        service.ativarColaborador(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador reativado com sucesso!"));
    }
```