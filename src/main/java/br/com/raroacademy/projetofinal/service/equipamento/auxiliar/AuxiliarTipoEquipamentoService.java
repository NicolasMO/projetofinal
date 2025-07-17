package br.com.raroacademy.projetofinal.service.equipamento.auxiliar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.exception.equipamento.tipoEquipamento.TipoEquipamentoDuplicadoException;
import br.com.raroacademy.projetofinal.exception.equipamento.tipoEquipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;

@Component
public class AuxiliarTipoEquipamentoService {
	
	@Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;
    
	public TipoEquipamento salvarTipoEquipamento(TipoEquipamento tipoEquipamento) {
		return tipoEquipamentoRepository.save(tipoEquipamento);
	}
	
	public Page<TipoEquipamento> listarTodos(Pageable paginacao) {
		Page<TipoEquipamento> tiposEquipamentos = tipoEquipamentoRepository.findAll(paginacao);

        if (tiposEquipamentos.isEmpty()) {
            throw new TipoEquipamentoNaoEncontradoException("Tipo de equipamento não encontrado.");
        }

        return tiposEquipamentos;
	}
	
	public TipoEquipamento buscarTipoPorNome(String nome) {
        return tipoEquipamentoRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new TipoEquipamentoNaoEncontradoException("Tipo de equipamento '" + nome + "' não encontrado."));
    }
	
	public void deletarTipoEquipamento(TipoEquipamento tipoEquipamento) {
    	tipoEquipamentoRepository.delete(tipoEquipamento);
    }
	
    public TipoEquipamento buscarTipoPorId(Long id) {
        return tipoEquipamentoRepository.findById(id)
            .orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(
            		"Tipo de Equipamento com ID " + id + " não encontrado."));
    }
    
	public void validarNomeDuplicado(String nome) {
	    if (tipoEquipamentoRepository.existsByNomeIgnoreCase(nome)) {
	        throw new TipoEquipamentoDuplicadoException(nome);
	    }
	}
	
}
