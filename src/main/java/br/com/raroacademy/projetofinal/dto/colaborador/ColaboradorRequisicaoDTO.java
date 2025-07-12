package br.com.raroacademy.projetofinal.dto.colaborador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.sql.Date;


public record ColaboradorRequisicaoDTO(


        @NotBlank @NotBlank
        String nome,
        @Email @NotBlank
        String email,
        @NotBlank @CPF
        String cpf,
        @NotBlank @NotNull
        String cargo,
        @NotBlank
        String telefone,

        EnderecoRequisicaoDTO endereco,

        @NotNull
        Date data_admissao,

        Date data_demissao


) {
}
