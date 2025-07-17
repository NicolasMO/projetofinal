package br.com.raroacademy.projetofinal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import br.com.raroacademy.projetofinal.exception.enums.StatusEquipamentoInvalidoException;

public enum STATUS_EQUIPAMENTO {
	DISPONIVEL,
	ENVIADO,
    SEM_ESTOQUE,
    DANIFICADO,
    EM_MANUTENCAO,
    ALOCADO;
	
	@JsonCreator
	public static STATUS_EQUIPAMENTO from(String status) {
	    if (status == null)
	        throw new StatusEquipamentoInvalidoException("null");

	    for (STATUS_EQUIPAMENTO st : values()) {
	        if (st.name().equalsIgnoreCase(status.trim()))
	            return st;
	    }

	    throw new StatusEquipamentoInvalidoException(status);
		}
	
    @JsonValue
    public String toJson() {
    	return name();
    	}
}
