package br.com.raroacademy.projetofinal.service.equipamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.AtualizarTipoEquipamentoEstoqueMinimoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.TipoEquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.TipoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoDuplicadoException;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;

@Service
public class TipoEquipamentoService {

    @Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;

    public void criar(TipoEquipamentoRequisicaoDTO dto) {
    	if (tipoEquipamentoRepository.existsByNomeIgnoreCase(dto.nome())) {
    	    throw new TipoEquipamentoDuplicadoException(dto.nome());
    	}
    	
        TipoEquipamento tipo = new TipoEquipamento(dto.nome(), dto.tempoConfiguracao(), dto.estoqueMinimo());
        tipoEquipamentoRepository.save(tipo);
    }

    public TipoEquipamentoRespostaDTO buscarPorId(Long id) {
        TipoEquipamento tipo = buscarTipoPorId(id);
        return new TipoEquipamentoRespostaDTO(tipo.getId(), tipo.getNome(), tipo.getTempoConfiguracao(), tipo.getEstoqueMinimo());
    }

    public Page<TipoEquipamentoRespostaDTO> listarTodos(Pageable pageable) {
        return tipoEquipamentoRepository.findAll(pageable)
        		.map(tipo -> new TipoEquipamentoRespostaDTO(tipo.getId(), tipo.getNome(), tipo.getTempoConfiguracao(), tipo.getEstoqueMinimo()));
    }
    
    public void atualizar(Long id, TipoEquipamentoRequisicaoDTO dto) {
        TipoEquipamento tipo = buscarTipoPorId(id);

        tipo.setNome(dto.nome());
        tipo.setTempoConfiguracao(dto.tempoConfiguracao());
        tipo.setEstoqueMinimo(dto.estoqueMinimo());

        tipoEquipamentoRepository.save(tipo);
    }
    
    public void atualizarEstoqueMinimo(Long id, AtualizarTipoEquipamentoEstoqueMinimoDTO dto) {
        TipoEquipamento tipo = buscarTipoPorId(id);
        tipo.setEstoqueMinimo(dto.estoqueMinimo());
        tipoEquipamentoRepository.save(tipo);
    }

    public void deletar(Long id) {
        TipoEquipamento tipo = buscarTipoPorId(id);
        tipoEquipamentoRepository.delete(tipo);
    }
    
    private TipoEquipamento buscarTipoPorId(Long id) {
        return tipoEquipamentoRepository.findById(id)
            .orElseThrow(() -> new TipoEquipamentoNaoEncontradoException("Tipo de Equipamento com ID " + id + " não encontrado."));
    }
}