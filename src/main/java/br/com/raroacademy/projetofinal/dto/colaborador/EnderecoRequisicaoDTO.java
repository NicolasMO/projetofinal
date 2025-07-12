package br.com.raroacademy.projetofinal.dto.colaborador;

public record EnderecoRequisicaoDTO(

        //@NotNull @NotBlank
        Long colaborador_id,

        String cep,

        String tipo_endereco,

        String logradouro,

        String bairro,

        String uf,

        String estado,

        String regiao
) {
}
