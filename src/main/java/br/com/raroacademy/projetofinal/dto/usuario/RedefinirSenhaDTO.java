package br.com.raroacademy.projetofinal.dto.usuario;

import br.com.raroacademy.projetofinal.validation.usuario.SenhaForte;
import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaDTO(
        @NotBlank(message = "O token é obrigatório.")
        String token,

        @SenhaForte
        String novaSenha
) {}
