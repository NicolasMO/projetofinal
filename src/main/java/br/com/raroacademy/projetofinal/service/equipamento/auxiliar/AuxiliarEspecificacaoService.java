package br.com.raroacademy.projetofinal.service.equipamento.auxiliar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.equipamento.especificacao.EspecificacaoRequisicaoDTO;
import br.com.raroacademy.projetofinal.exception.equipamento.especificacao.EspecificacaoDuplicadaException;
import br.com.raroacademy.projetofinal.exception.equipamento.especificacao.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.repository.equipamento.EspecificacaoRepository;

@Component
public class AuxiliarEspecificacaoService {

	@Autowired
	private EspecificacaoRepository especificacaoRepository;
	
	public Especificacao salvarEspecificacao(Especificacao especificacao) {
		return especificacaoRepository.save(especificacao);
	}
	
	public Page<Especificacao> listarTodos(Pageable paginacao) {	
		Page<Especificacao> especificacoes = especificacaoRepository.findAll(paginacao);

        if (especificacoes.isEmpty()) {
            throw new EspecificacaoNaoEncontradaException("Especificação não encontrada.");
        }

        return especificacoes;
	}
	
	public Especificacao buscarEspecificacaoPorId(Long id) {
		Especificacao espececificacao = especificacaoRepository.findById(id)
	        .orElseThrow(() -> new EspecificacaoNaoEncontradaException("Especificação com ID " + id + " não encontrada."));
		return espececificacao;
	}
	
	public Especificacao buscarEspecificacaoPorDescricaoEValor(String descricao, String valor) {
	    Especificacao especificacao = especificacaoRepository.findByDescricaoIgnoreCaseAndValorIgnoreCase(descricao, valor)
	        .orElseThrow(() -> new EspecificacaoNaoEncontradaException("Especificação não encontrada."));
	    return especificacao;
	}
	
	public void deletarEspecificaocao(Especificacao especificacao) {
		especificacaoRepository.delete(especificacao);
    }

	public void validarEspecificacaoPorDescricaoEValor(EspecificacaoRequisicaoDTO dto) {
		boolean existeEspecificacao = especificacaoRepository.existsByDescricaoAndValor(dto.descricao(), dto.valor());

	    if (existeEspecificacao) {
	        throw new EspecificacaoDuplicadaException("Especificação já cadastrada.");
	    }		
	}
	
}
