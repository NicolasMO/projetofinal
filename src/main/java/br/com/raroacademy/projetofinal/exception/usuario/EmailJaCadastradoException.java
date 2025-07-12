package br.com.raroacademy.projetofinal.exception.usuario;


public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
