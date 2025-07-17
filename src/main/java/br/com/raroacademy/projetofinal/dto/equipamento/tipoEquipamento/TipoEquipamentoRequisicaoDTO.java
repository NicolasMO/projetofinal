package br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.raroacademy.projetofinal.config.desserializador.DesserializadorInteiro;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TipoEquipamentoRequisicaoDTO(
		@NotNull(message = "O nome é obrigatório")
		@NotBlank(message = "O nome não pode ser vazio")
        String nome,
        
        @JsonDeserialize(using = DesserializadorInteiro.class)
        @NotNull(message = "O tempo de configuração é obrigatório")
        @Min(value = 0, message = "O tempo de configuração deve ser 0 ou maior.") 
        Integer tempoConfiguracao,
        
        @JsonDeserialize(using = DesserializadorInteiro.class)
        @Min(value = 0, message = "O estoque mínimo deve ser 0 ou maior.")
        Integer estoqueMinimo
) {}
