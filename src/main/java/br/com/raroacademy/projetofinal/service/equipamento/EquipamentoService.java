package br.com.raroacademy.projetofinal.service.equipamento;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoAtualizarDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.exception.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.equipamento.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.exception.equipamento.NumeroSerieDuplicadoException;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.EspecificacaoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.service.estoque.EstoqueService;
import jakarta.validation.constraints.NotNull;

@Service
public class EquipamentoService {

	@Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;

    @Autowired
    private EspecificacaoRepository especificacaoRepository;

    @Autowired
    private EstoqueService estoqueService;


    public void criar(EquipamentoRequisicaoDTO dto) {
    	
    	// Verifica se já existe aquele número de série cadastrado
    	if (equipamentoRepository.existsById(dto.numeroSerie())) {
    		throw new NumeroSerieDuplicadoException("Já existe um equipamento com esse número de série: " + dto.numeroSerie());
        }
    	
    	// buscar o tipo de equipamento
        TipoEquipamento tipo = buscarTipoPorId(dto.tipoEquipamentoId());

        // buscar as especificacoes
        Set<Especificacao> especificacoes = new HashSet<>(especificacaoRepository.findAllById(dto.especificacoesIds()));

        if (especificacoes.size() != dto.especificacoesIds().size()) {
            throw new EspecificacaoNaoEncontradaException("Uma ou mais especificações fornecidas não foram encontradas.");
        }
        
        // criar o objeto
        Equipamento novoEquipamento = new Equipamento(
        		dto.numeroSerie(),
                dto.modelo(),
                dto.marca(),
                dto.dataAquisicao(),
                0, // tempo de uso sempre zero para produtos novos
                STATUS_EQUIPAMENTO.DISPONIVEL, // status inicial
                tipo,
                especificacoes
        );

        equipamentoRepository.save(novoEquipamento);

        estoqueService.verificarEstoquePorTipo(tipo.getId());

    }

    public EquipamentoRespostaDTO buscarPorNumeroSerie(String numeroSerie) {
    	Equipamento equipamento = buscarEquipamentoPorNumSerie(numeroSerie);
	    return mapearParaDTO(equipamento);
    }

    public Page<EquipamentoRespostaDTO> listarTodos(Pageable pageable) {
        return equipamentoRepository.findAll(pageable)
        		.map(this::mapearParaDTO);
    }
    
    public EquipamentoRespostaDTO atualizar(String numeroSerie, EquipamentoAtualizarDTO dto, STATUS_EQUIPAMENTO status) {
        Equipamento equipamento = buscarEquipamentoPorNumSerie(numeroSerie);

        TipoEquipamento tipo = buscarTipoPorId(dto.tipoEquipamentoId());

        Set<Especificacao> especificacoes = new HashSet<>(especificacaoRepository.findAllById(dto.especificacoesIds()));
        
        if (especificacoes.size() != dto.especificacoesIds().size()) {
            throw new EspecificacaoNaoEncontradaException("Uma ou mais especificações fornecidas não foram encontradas.");
        }

        equipamento.setModelo(dto.modelo());
        equipamento.setMarca(dto.marca());
        equipamento.setTipoEquipamento(tipo);
        equipamento.setEspecificacoes(especificacoes);
        equipamento.setStatus(status);

        Equipamento atualizado = equipamentoRepository.save(equipamento);

        return mapearParaDTO(atualizado);
    }
    
    public void deletar(String numeroSerie) {
    	// Verifica se existe um equipamento com aquele número de série para realizar exclusão, se não achar joga o erro
        Equipamento equipamento = buscarEquipamentoPorNumSerie(numeroSerie);
        TipoEquipamento tipo = equipamento.getTipoEquipamento();

        equipamentoRepository.delete(equipamento);

        estoqueService.verificarEstoquePorTipo(tipo.getId());
    }
    
    private EquipamentoRespostaDTO mapearParaDTO(Equipamento equipamento) {
        List<EspecificacaoRespostaDTO> especificacoes = equipamento.getEspecificacoes()
            .stream()
            .map(espec -> new EspecificacaoRespostaDTO(espec.getId(), espec.getDescricao(), espec.getValor()))
            .toList();

        return new EquipamentoRespostaDTO(
            equipamento.getNumeroSerie(),
            equipamento.getModelo(),
            equipamento.getMarca(),
            equipamento.getDataAquisicao(),
            equipamento.getTempoUso(),
            equipamento.getStatus(),
            equipamento.getTipoEquipamento().getNome(),
            especificacoes
        );
    }
    

	private Equipamento buscarEquipamentoPorNumSerie(String numeroSerie) {
		Equipamento equipamento = equipamentoRepository.findById(numeroSerie)
    			.orElseThrow(() -> new EquipamentoNaoEncontradoException("Equipamento com número de série '" + numeroSerie + "' não encontrado."));
		return equipamento;
	}

	private TipoEquipamento buscarTipoPorId(@NotNull Long tipoEquipamentoId) {
		TipoEquipamento tipo = tipoEquipamentoRepository.findById(tipoEquipamentoId)
        		.orElseThrow(() -> new TipoEquipamentoNaoEncontradoException("Tipo de equipamento com ID " + tipoEquipamentoId + " não encontrado"));
		return tipo;
	}
    
}