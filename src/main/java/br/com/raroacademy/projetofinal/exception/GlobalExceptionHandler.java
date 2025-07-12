package br.com.raroacademy.projetofinal.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.raroacademy.projetofinal.dto.response.ApiErroResposta;
import br.com.raroacademy.projetofinal.exception.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.equipamento.EspecificacaoDuplicadaException;
import br.com.raroacademy.projetofinal.exception.equipamento.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.exception.equipamento.NumeroSerieDuplicadoException;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoDuplicadoException;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.usuario.EmailJaCadastradoException;
import br.com.raroacademy.projetofinal.exception.usuario.SenhaRepetidaException;
import br.com.raroacademy.projetofinal.exception.usuario.TokenExpiradoOuUtilizadoException;
import br.com.raroacademy.projetofinal.exception.usuario.TokenInvalidoException;
import br.com.raroacademy.projetofinal.exception.usuario.UsuarioNaoEncontradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ApiErroResposta> tratarUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
                HttpStatus.NOT_FOUND.value(),
                "Usuário não encontrado",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

        @ExceptionHandler(EmailJaCadastradoException.class)
        public ResponseEntity<ApiErroResposta> tratarEmailJaCadastrado(EmailJaCadastradoException ex) {
            ApiErroResposta resposta = new ApiErroResposta(
                    HttpStatus.BAD_REQUEST.value(),
                    "Email já cadastrado",
                    ex.getMessage(),
                    Collections.singletonList("Informe um e-mail diferente")
            );
            return ResponseEntity.badRequest().body(resposta);
        }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErroResposta> tratarValidacoes(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErroResposta resposta = new ApiErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                "Ocorreram erros nos dados enviados",
                erros
        );

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErroResposta> tratarErrosGerais(Exception ex) {
        ApiErroResposta resposta = new ApiErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Ocorreu um erro inesperado",
                Collections.singletonList(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ApiErroResposta> tratarTokenInvalido(TokenInvalidoException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                "Token inválido",
                ex.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(TokenExpiradoOuUtilizadoException.class)
    public ResponseEntity<ApiErroResposta> tratarTokenExpirado(TokenExpiradoOuUtilizadoException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                "Token expirado ou já utilizado",
                ex.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(SenhaRepetidaException.class)
    public ResponseEntity<ApiErroResposta> tratarSenhaRepetida(SenhaRepetidaException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                "Senha inválida",
                ex.getMessage(),
                List.of("Escolha uma senha diferente da anterior.")
        );
        return ResponseEntity.badRequest().body(resposta);
    }

    
    // Exceptions de Equipamentos
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErroResposta> tratarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        String nome = ex.getName();
        String tipoEsperado = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valor esperado";
        String valorRecebido;

        if (ex.getValue() instanceof Object[] arr) {
            valorRecebido = Arrays.stream(arr).map(Object::toString).collect(Collectors.joining(", "));
        } else {
            valorRecebido = ex.getValue() != null ? ex.getValue().toString() : "nulo";
        }

        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.BAD_REQUEST.value(),
            "Parâmetro inválido",
            "O parâmetro '" + nome + "' recebeu um ou mais valores inválidos.",
            List.of("Valor(s) recebido(s): '" + valorRecebido + "'. Esperava um " + tipoEsperado + " válido.")
        );
        return ResponseEntity.badRequest().body(resposta);
    }

    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErroResposta> tratarIllegalArgumentException(IllegalArgumentException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.BAD_REQUEST.value(),
            "Parâmetro inválido",
            ex.getMessage(),
            List.of(ex.getMessage())
        );
        return ResponseEntity.badRequest().body(resposta);
    }
    
    @ExceptionHandler(EspecificacaoNaoEncontradaException.class)
    public ResponseEntity<ApiErroResposta> tratarEspecificacaoNaoEncontrada(EspecificacaoNaoEncontradaException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.NOT_FOUND.value(),
            "Especificação não encontrada",
            ex.getMessage(),
            List.of("Especificação não encontrada.")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(EspecificacaoDuplicadaException.class)
    public ResponseEntity<ApiErroResposta> tratarEspecificacaoDuplicada(EspecificacaoDuplicadaException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.BAD_REQUEST.value(),
            "Especificação duplicada",
            ex.getMessage(),
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
    
    @ExceptionHandler(TipoEquipamentoNaoEncontradoException.class)
    public ResponseEntity<ApiErroResposta> tratarTipoEquipamentoNaoEncontrado(TipoEquipamentoNaoEncontradoException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.NOT_FOUND.value(),
            "Tipo de equipamento não encontrado",
            ex.getMessage(),
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }
    
    @ExceptionHandler(TipoEquipamentoDuplicadoException.class)
    public ResponseEntity<ApiErroResposta> tratarTipoEquipamentoDuplicado(TipoEquipamentoDuplicadoException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.BAD_REQUEST.value(),
            "Tipo de equipamento duplicado",
            ex.getMessage(),
            List.of(ex.getMessage())
        );
        return ResponseEntity.badRequest().body(resposta);
    }
    
    @ExceptionHandler(EquipamentoNaoEncontradoException.class)
    public ResponseEntity<ApiErroResposta> tratarEquipamentoNaoEncontrado(EquipamentoNaoEncontradoException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.NOT_FOUND.value(),
            "Equipamento não encontrado",
            ex.getMessage(),
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(NumeroSerieDuplicadoException.class)
    public ResponseEntity<ApiErroResposta> tratarNumeroSerieDuplicado(NumeroSerieDuplicadoException ex) {
        ApiErroResposta resposta = new ApiErroResposta(
            HttpStatus.CONFLICT.value(),
            "Número de série duplicado",
            ex.getMessage(),
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }
}