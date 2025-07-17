package br.com.raroacademy.projetofinal.service.equipamento.auxiliar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.exception.equipamento.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.equipamento.equipamento.NumeroSerieDuplicadoException;
import br.com.raroacademy.projetofinal.exception.equipamento.especificacao.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.exception.equipamento.tipoEquipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.EspecificacaoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import jakarta.validation.constraints.NotNull;

@Component
public class AuxiliarEquipamentoService {

	@Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;

    @Autowired
    private EspecificacaoRepository especificacaoRepository;

    public Equipamento salvarEquipamento(Equipamento equipamento) {
        return equipamentoRepository.save(equipamento);
    }
    
    public Page<Equipamento> listarTodos(Pageable paginacao) {
    	Page<Equipamento> equipamentos = equipamentoRepository.findAll(paginacao);

        if (equipamentos.isEmpty()) {
            throw new EquipamentoNaoEncontradoException("Nenhum equipamento encontrado.");
        }

        return equipamentos;
    }
    
    public void deletarEquipamento(String numeroSerie) {
    	equipamentoRepository.delete(buscarEquipamentoPorNumSerie(numeroSerie));
    }
    
    public Equipamento buscarEquipamentoPorNumSerie(String numeroSerie) {
        return equipamentoRepository.findById(numeroSerie)
            .orElseThrow(() -> new EquipamentoNaoEncontradoException(
                "Equipamento com número de série '" + numeroSerie + "' não encontrado."));
    }

    public TipoEquipamento buscarTipoPorId(@NotNull Long tipoEquipamentoId) {
        return tipoEquipamentoRepository.findById(tipoEquipamentoId)
            .orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(
                "Tipo de equipamento com ID " + tipoEquipamentoId + " não encontrado"));
    }

    public Set<Especificacao> buscarEspecificacoesValidas(List<Long> ids) {
        Set<Especificacao> especificacoes = new HashSet<>(especificacaoRepository.findAllById(ids));
        if (especificacoes.size() != ids.size()) {
            throw new EspecificacaoNaoEncontradaException("Especificação não encontrada.");
        }
        return especificacoes;
    }

    public void validarNumeroSerieNaoExistente(String numeroSerie) {
        if (equipamentoRepository.existsById(numeroSerie)) {
            throw new NumeroSerieDuplicadoException(
                "Já existe um equipamento com esse número de série: " + numeroSerie
            );
        }
    }
    
}
