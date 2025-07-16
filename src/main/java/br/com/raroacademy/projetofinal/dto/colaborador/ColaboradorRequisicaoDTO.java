package br.com.raroacademy.projetofinal.dto.colaborador;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.sql.Date;

public record ColaboradorRequisicaoDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @NotNull(message = "nome não pode ser nulo!")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O nome deve conter apenas letras e espaços.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        @NotBlank(message = "O CPF é obrigatório.")
        @CPF(message = "CPF inválido. Insira o CPF valido e sem pontuação,")
        @Size(max = 11, message = "O CPF sem pontuação deve ter 11 números", min = 11)
        String cpf,

        @NotBlank(message = "O cargo é obrigatório.")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O cargo deve conter apenas letras e espaços.")
        String cargo,

        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(
                regexp = "\\(\\d{2}\\) (\\d{4}|\\d{5})-\\d{4}",
                message = "Formato do telefone inválido. Use (61) 9999-0000 ou (61) 99999-0000"
        )
        String telefone,

        @NotNull(message = "O endereço é obrigatório.")
        @Valid
        EnderecoRequisicaoDTO endereco,

        @NotNull(message = "A data de admissão é obrigatória.")
        Date data_admissao,
        @NotNull(message = "a data de demissão é obrigatória.")
        Date data_demissao

) {}
