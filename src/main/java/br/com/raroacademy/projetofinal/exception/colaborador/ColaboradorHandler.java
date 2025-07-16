package br.com.raroacademy.projetofinal.exception.colaborador;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class ColaboradorHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleViolacaoUnica(DataIntegrityViolationException ex) {
        String mensagemErro = extrairMensagemDeDuplicidade(ex);

        Map<String, Object> corpo = Map.of(
                "status", 400,
                "erro", "Violação de regra de integridade",
                "mensagem", mensagemErro,
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(corpo);
    }

    private String extrairMensagemDeDuplicidade(DataIntegrityViolationException ex) {
        String mensagemRaiz = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse("")
                .toLowerCase();

        if (mensagemRaiz.contains("colaborador_email_key")) {
            return "Este e-mail já está em uso.";
        }
        if (mensagemRaiz.contains("colaborador_cpf_key")) {
            return "Este CPF já está cadastrado.";
        }

        if (mensagemRaiz.contains("constraint") || mensagemRaiz.contains("duplicate")) {
            return "Violação de dados: valor duplicado em campo único.";
        }

        return "Erro de integridade referencial. Verifique os dados enviados.";
    }
}

