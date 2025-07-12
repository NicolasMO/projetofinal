package br.com.raroacademy.projetofinal.exception.usuario;

public class SenhaRepetidaException extends RuntimeException {
    public SenhaRepetidaException(String mensagem) {
        super(mensagem);
    }
}
