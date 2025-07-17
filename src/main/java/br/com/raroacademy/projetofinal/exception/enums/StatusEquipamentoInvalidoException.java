package br.com.raroacademy.projetofinal.exception.enums;

import java.util.Arrays;

import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;

public class StatusEquipamentoInvalidoException extends RuntimeException {
    public StatusEquipamentoInvalidoException(String valorInvalido) {
        super("Status inválido: '" + valorInvalido + "'. Os valores permitidos são: " +
              Arrays.toString(STATUS_EQUIPAMENTO.values()));
    }
}