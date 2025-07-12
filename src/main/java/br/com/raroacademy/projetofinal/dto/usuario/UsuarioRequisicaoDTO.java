package br.com.raroacademy.projetofinal.dto.usuario;

import br.com.raroacademy.projetofinal.validation.usuario.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequisicaoDTO(
        @NotBlank(message = "Nome não pode ficar em branco") String nome,
        @Email(message = "Formato de e-mail inválido")
        @NotBlank(message = "E-mail não pode ficar em branco")
        String email,

        @SenhaForte
        String senha

) {}