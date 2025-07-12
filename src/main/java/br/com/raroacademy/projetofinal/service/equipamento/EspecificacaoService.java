package br.com.raroacademy.projetofinal.service.equipamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.EspecificacaoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.exception.equipamento.EspecificacaoDuplicadaException;
import br.com.raroacademy.projetofinal.exception.equipamento.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.repository.equipamento.EspecificacaoRepository;
import jakarta.validation.Valid;

@Service
public class EspecificacaoService {

	@Autowired
	private EspecificacaoRepository especificacaoRepository;
	
	public void criar(EspecificacaoRequisicaoDTO dto) {
		boolean existe = especificacaoRepository.existsByDescricaoAndValor(dto.descricao(), dto.valor());

	    if (existe) {
	        throw new EspecificacaoDuplicadaException("Especificação já cadastrada.");
	    }

	    Especificacao espec = new Especificacao(dto.descricao(), dto.valor());
	    especificacaoRepository.save(espec);
	}

	public EspecificacaoRespostaDTO buscarPorId(Long id) {
	    Especificacao espec = buscarEspecificacaoPorId(id);
	    return new EspecificacaoRespostaDTO(espec.getId(), espec.getDescricao(), espec.getValor());
	}

	public Page<EspecificacaoRespostaDTO> listarTodos(Pageable paginacao) {
		return especificacaoRepository.findAll(paginacao)
	            .map(espec -> new EspecificacaoRespostaDTO(espec.getId(), espec.getDescricao(), espec.getValor()));
	}
	
	public void atualizar(Long id, @Valid EspecificacaoRequisicaoDTO dto) {
	    Especificacao espec = buscarEspecificacaoPorId(id);

        espec.setDescricao(dto.descricao());
        espec.setValor(dto.valor());

        especificacaoRepository.save(espec);
    }

	public void deletar(Long id) {
	    if (!especificacaoRepository.existsById(id)) {
	    	throw new EspecificacaoNaoEncontradaException("Especificação com ID " + id + " não encontrada.");
	    }
	    especificacaoRepository.deleteById(id);
	}
	
	private Especificacao buscarEspecificacaoPorId(Long id) {
		Especificacao espec = especificacaoRepository.findById(id)
	        .orElseThrow(() -> new EspecificacaoNaoEncontradaException("Especificação com ID " + id + " não encontrada."));
		return espec;
	}

}
