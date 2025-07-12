package br.com.raroacademy.projetofinal.dto.equipamento;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.raroacademy.projetofinal.config.desserializador.DesserializadorListaLong;
import br.com.raroacademy.projetofinal.config.desserializador.DesserializadorLong;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoAtualizarDTO(
		@NotNull(message = "O modelo é obrigatório.")
		@NotBlank(message = "A modelo não pode ser vazio.")
        String modelo,
        
        @NotNull(message = "A marca é obrigatória.")
        @NotBlank(message = "A marca não pode ser vazia.")
        String marca,
 
        @NotNull
        @JsonDeserialize(using = DesserializadorLong.class)
		@NotNull(message = "O ID do tipo de equipamento é obrigatório.")
        Long tipoEquipamentoId,
 
        @NotNull
        @JsonDeserialize(using = DesserializadorListaLong.class)
		@NotNull(message = "A lista de especificações é obrigatória.")
        List<Long> especificacoesIds
) {}
