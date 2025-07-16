package br.com.raroacademy.projetofinal.dto.colaborador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.sql.Date;

public record ColaboradorAtualizacaoParcialDTO(

        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O nome deve conter apenas letras e espaços.")
        String nome,
        @Email
        String email,
        @CPF(message = "CPF inválido. Insira o CPF valido e sem pontuação,")
        String cpf,
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O cargo deve conter apenas letras e espaços.")
        String cargo,

        @Pattern(
                regexp = "\\(\\d{2}\\) (\\d{4}|\\d{5})-\\d{4}",
                message = "Formato do telefone inválido. Use (61) 9999-0000 ou (61) 99999-0000"
        )
        String telefone,
        Date data_admissao,
        Date data_demissao,
        EnderecoParcialDTO endereco
) {}