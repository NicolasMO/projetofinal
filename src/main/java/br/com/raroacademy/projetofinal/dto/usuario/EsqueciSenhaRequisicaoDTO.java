package br.com.raroacademy.projetofinal.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaRequisicaoDTO(
        @NotBlank(message = "O campo 'email' é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String email
) {}
