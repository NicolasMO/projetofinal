package br.com.raroacademy.projetofinal.service.equipamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.especificacao.EspecificacaoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.especificacao.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.mapper.equipamento.MapeadorDeEspecificacoes;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.service.equipamento.auxiliar.AuxiliarEspecificacaoService;
import jakarta.validation.Valid;

@Service
public class EspecificacaoService {

	@Autowired
	private AuxiliarEspecificacaoService auxiliarEspecificacaoService;
	
	
	public EspecificacaoRespostaDTO criar(EspecificacaoRequisicaoDTO dto) {
		auxiliarEspecificacaoService.validarEspecificacaoPorDescricaoEValor(dto);
		
	    Especificacao especificacao = new Especificacao(dto.descricao(), dto.valor());
	    auxiliarEspecificacaoService.salvarEspecificacao(especificacao);
	    
	    return MapeadorDeEspecificacoes.paraEspecificacaoRespostaDTO(especificacao);
	}

	public EspecificacaoRespostaDTO buscarPorId(Long id) {
	    return MapeadorDeEspecificacoes.paraEspecificacaoRespostaDTO(auxiliarEspecificacaoService.buscarEspecificacaoPorId(id));
	}

	public Page<EspecificacaoRespostaDTO> listarTodos(Pageable paginacao) {
		return auxiliarEspecificacaoService.listarTodos(paginacao)
				.map(MapeadorDeEspecificacoes::paraEspecificacaoRespostaDTO);
	}
	
	public EspecificacaoRespostaDTO atualizar(Long id, @Valid EspecificacaoRequisicaoDTO dto) {
	    Especificacao especificacao = auxiliarEspecificacaoService.buscarEspecificacaoPorId(id);
	    especificacao.atualizarDados(dto.descricao(), dto.valor());
        auxiliarEspecificacaoService.salvarEspecificacao(especificacao);
        
        return MapeadorDeEspecificacoes.paraEspecificacaoRespostaDTO(especificacao);
    }

	public void deletar(Long id) {
	    Especificacao especificacao = auxiliarEspecificacaoService.buscarEspecificacaoPorId(id);
	    auxiliarEspecificacaoService.deletarEspecificaocao(especificacao);
	}
	
}
