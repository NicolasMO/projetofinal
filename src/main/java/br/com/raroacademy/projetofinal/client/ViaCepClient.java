package br.com.raroacademy.projetofinal.client;

import br.com.raroacademy.projetofinal.dto.colaborador.EnderecoRequisicaoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://viacep.com.br/ws/", name = "viacep")
public interface ViaCepClient {
    @GetMapping(path = "{cep}/json/")
    EnderecoRequisicaoDTO getEndereco(@PathVariable String cep);
}
