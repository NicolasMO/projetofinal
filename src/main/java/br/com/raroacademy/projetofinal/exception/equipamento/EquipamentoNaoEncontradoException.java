package br.com.raroacademy.projetofinal.exception.equipamento;

public class EquipamentoNaoEncontradoException extends RuntimeException {
	public EquipamentoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
