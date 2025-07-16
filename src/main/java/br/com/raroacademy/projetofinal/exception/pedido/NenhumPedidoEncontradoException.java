package br.com.raroacademy.projetofinal.exception.pedido;

public class NenhumPedidoEncontradoException extends RuntimeException {
    public NenhumPedidoEncontradoException(String mensagem) {
        super(mensagem);
    }
}