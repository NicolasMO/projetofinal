package br.com.raroacademy.projetofinal.exception.usuario;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String mensagem) {
        super(mensagem);
    }
}
