package br.com.raroacademy.projetofinal.dto.equipamento;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.raroacademy.projetofinal.config.desserializador.DesserializadorListaLong;
import br.com.raroacademy.projetofinal.config.desserializador.DesserializadorLong;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoRequisicaoDTO(
		@NotNull @NotBlank
        String numeroSerie,
        
        @NotNull @NotBlank
        String modelo,
        
        @NotNull @NotBlank
        String marca,
        
        @NotNull
        LocalDate dataAquisicao,
        
        @NotNull
        @JsonDeserialize(using = DesserializadorLong.class)
        Long tipoEquipamentoId,
        
        @NotNull
        @JsonDeserialize(using = DesserializadorListaLong.class)
        List<Long> especificacoesIds
) {}
