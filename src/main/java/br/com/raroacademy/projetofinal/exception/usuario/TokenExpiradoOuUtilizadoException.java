package br.com.raroacademy.projetofinal.exception.usuario;

public class TokenExpiradoOuUtilizadoException extends RuntimeException {
    public TokenExpiradoOuUtilizadoException(String mensagem) {
        super(mensagem);
    }
}
