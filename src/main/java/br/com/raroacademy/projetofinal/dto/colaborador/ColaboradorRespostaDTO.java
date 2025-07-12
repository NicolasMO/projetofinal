package br.com.raroacademy.projetofinal.dto.colaborador;

import java.sql.Date;

public record ColaboradorRespostaDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String cargo,
        String telefone,
        Date dataAdmissao,
        Date dataDemissao,
        EnderecoRespostaDTO enderecos

) {
}
