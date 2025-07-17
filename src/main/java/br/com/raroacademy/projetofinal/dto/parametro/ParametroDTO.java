package br.com.raroacademy.projetofinal.dto.parametro;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record ParametroDTO(
        @NotNull @Min(0) Integer tempoMedioReposicaoDias,
        @NotNull @Min(0) Integer tempoMedioConsumoEstoqueDias,
        @NotNull @DecimalMin("0.0") Double taxaMediaDefeituososPercentual,

        @NotEmpty(message = "temposEnvio não pode ser vazio")
        @Valid List<TempoEnvioRegiaoDTO> temposEnvio,

        @NotNull(message = "tipoEquipamentoId é obrigatório")
        Long tipoEquipamentoId
) {}
