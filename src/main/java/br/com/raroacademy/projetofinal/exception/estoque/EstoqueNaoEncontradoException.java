package br.com.raroacademy.projetofinal.exception.estoque;

public class EstoqueNaoEncontradoException extends RuntimeException {
	public EstoqueNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
