package br.com.raroacademy.projetofinal.exception.alocacao.equipamento;

public class EquipamentoAlocadoNaoEncontradoException extends RuntimeException {
	public EquipamentoAlocadoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
