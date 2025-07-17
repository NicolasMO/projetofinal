package br.com.raroacademy.projetofinal.service.alocacao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRecebimentoDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRelatorioDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoResumoProjection;
import br.com.raroacademy.projetofinal.exception.alocacao.equipamento.EquipamentoAlocadoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.equipamento.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.mapper.alocacao.AlocacaoEquipamentoMapper;
import br.com.raroacademy.projetofinal.model.alocacao.AlocacaoEquipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.repository.alocacao.AlocacaoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import jakarta.transaction.Transactional;

@Service
public class AlocacaoEquipamentoService {

	@Autowired
    private AlocacaoEquipamentoRepository alocacaoEquipamentoRepository;
	
	@Autowired
	private EquipamentoRepository equipamentoRepository;
	
	@Autowired
    private AlocacaoEquipamentoMapper alocacaoEquipamentoMapper;

    public Page<AlocacaoEquipamentoRespostaDTO> listarTodos(Pageable paginacao) {
        return alocacaoEquipamentoRepository.findAll(paginacao)
        		.map(alocacaoEquipamento -> new AlocacaoEquipamentoRespostaDTO(
        				alocacaoEquipamento.getId(),
        				alocacaoEquipamento.getEquipamento().getNumeroSerie(),
        				alocacaoEquipamento.getEquipamento().getModelo(),
        				alocacaoEquipamento.getEquipamento().getMarca(),
        				alocacaoEquipamento.getColaborador().getNome(),
        				alocacaoEquipamento.getDataEntregaPrevista(),
        				alocacaoEquipamento.getDataEntrega(),
        				alocacaoEquipamento.getDataDevolucaoPrevista(),
        				alocacaoEquipamento.getDataDevolucao(),
        				alocacaoEquipamento.isDevolvido(),
        				alocacaoEquipamento.getStatusEquipamento(),
				        alocacaoEquipamento.getObservacao()));
    }
    
    public AlocacaoEquipamentoRelatorioDTO gerarRelatorioDeEquipamentosAlocadosComDevolucaoPrevistaPorPeriodo(LocalDate inicio, LocalDate fim, Pageable paginacao) {
    	validarIntervaloDatas(inicio, fim);
    	
    	AlocacaoEquipamentoResumoProjection resumo = alocacaoEquipamentoRepository.buscarResumo(inicio, fim);

        Page<AlocacaoEquipamento> alocacaoEquipamentoPaginado = alocacaoEquipamentoRepository.findByDataDevolucaoPrevistaBetween(inicio, fim, paginacao);
        
        if (alocacaoEquipamentoPaginado.isEmpty()) {
            throw new EquipamentoAlocadoNaoEncontradoException("Nenhum equipamento alocado com devolução prevista encontrado no período informado.");
        }

        List<AlocacaoEquipamentoRespostaDTO> detalhes = alocacaoEquipamentoPaginado.getContent().stream()
            .map(alocacaoEquipamentoMapper::paraRespostaDTO)
            .toList();

        return new AlocacaoEquipamentoRelatorioDTO(
        		resumo.getTotalPrevisto(),
                resumo.getTotalEntregue(),
                resumo.getTotalPendente(),
                alocacaoEquipamentoPaginado.getNumber(),
                alocacaoEquipamentoPaginado.getTotalPages() - 1,
                alocacaoEquipamentoPaginado.getTotalElements(),
                alocacaoEquipamentoPaginado.getSize(),
                detalhes
            );
    }
   
    public AlocacaoEquipamentoRelatorioDTO buscarEquipamentosPorColaborador(Long colaboradorId, LocalDate inicio, LocalDate fim, Pageable paginacao) {
    	
        validarIntervaloDatas(inicio, fim);

        Page<AlocacaoEquipamento> alocacaoEquipamentoPaginado = validarConsultaComDados(colaboradorId, inicio, fim, paginacao);
        
        if (alocacaoEquipamentoPaginado.isEmpty()) {
            throw new EquipamentoAlocadoNaoEncontradoException("Nenhum equipamento alocado com devolução prevista encontrado no período informado.");
        }
        
	    long totalPrevisto = alocacaoEquipamentoPaginado.getTotalElements();
	    long totalDevolvido = alocacaoEquipamentoPaginado.getContent().stream().filter(AlocacaoEquipamento::isDevolvido).count();
	    long totalPendente = totalPrevisto - totalDevolvido;

	    List<AlocacaoEquipamentoRespostaDTO> detalhes = alocacaoEquipamentoPaginado.getContent().stream()
	        .map(alocacaoEquipamentoMapper::paraRespostaDTO)
	        .toList();
	    	
        return new AlocacaoEquipamentoRelatorioDTO(
        		totalPrevisto,
                totalDevolvido,
                totalPendente,
                alocacaoEquipamentoPaginado.getNumber(),
                alocacaoEquipamentoPaginado.getTotalPages() - 1,
                alocacaoEquipamentoPaginado.getTotalElements(),
                alocacaoEquipamentoPaginado.getSize(),
                detalhes
        );
    }
    
    @Transactional
    public void confirmarRecebimentoEquipamento(AlocacaoEquipamentoRecebimentoDTO dto) {
    
    	AlocacaoEquipamento alocacaoEquipamento = validacaoEquipamentoJaCadastrado(dto);
        
        Equipamento equipamento = equipamentoRepository.findById(alocacaoEquipamento.getEquipamento().getNumeroSerie())
            .orElseThrow(() -> new EquipamentoNaoEncontradoException("Equipamento não encontrado."));

        equipamento.setStatus(dto.statusEquipamento());
        equipamentoRepository.save(equipamento);

        alocacaoEquipamento.setStatusEquipamento(dto.statusEquipamento());
        alocacaoEquipamento.setObservacao(dto.observacao());
        alocacaoEquipamento.setDataDevolucao(LocalDate.now());
        alocacaoEquipamento.setDevolvido(true);

        alocacaoEquipamentoRepository.save(alocacaoEquipamento);
    }

	private AlocacaoEquipamento validacaoEquipamentoJaCadastrado(AlocacaoEquipamentoRecebimentoDTO dto) {
		AlocacaoEquipamento alocacaoEquipamento = alocacaoEquipamentoRepository.findById(dto.alocacaoEquipamentoId())
    	        .orElseThrow(() -> new EquipamentoAlocadoNaoEncontradoException("Alocação não encontrada."));
    	
    	if(alocacaoEquipamento.isDevolvido()) {
    		throw new IllegalArgumentException("Equipamento já devolvido.");
    	}
    	
    	String numeroSerieAlocado = alocacaoEquipamento.getEquipamento().getNumeroSerie();

        if (!numeroSerieAlocado.equals(dto.numeroSerie())) {
            throw new IllegalArgumentException("Número de série informado não corresponde ao equipamento alocado.");
        }
		return alocacaoEquipamento;
	}
    
	private Page<AlocacaoEquipamento> validarConsultaComDados(Long colaboradorId, LocalDate inicio, LocalDate fim,
			Pageable paginacao) {
		Page<AlocacaoEquipamento> page;

        if (inicio != null && fim != null) {
            page = alocacaoEquipamentoRepository.findByColaboradorIdAndDataDevolucaoPrevistaBetween(
                colaboradorId, inicio, fim, paginacao);
        } else {
            // Sem filtro de data, busca tudo do colaborador
            page = alocacaoEquipamentoRepository.findByColaboradorId(colaboradorId, paginacao);
        }
		return page;
	}

	private void validarIntervaloDatas(LocalDate inicio, LocalDate fim) {
		if ((inicio == null) != (fim == null)) {
            throw new IllegalArgumentException("Para filtrar por data, informe as duas datas: início e fim.");
        }

        if (inicio != null && fim != null) {
            if (fim.isBefore(inicio)) {
                throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início.");
            }
            long dias = ChronoUnit.DAYS.between(inicio, fim);
            if (dias > 90) {
                throw new IllegalArgumentException("O intervalo máximo permitido é de 90 dias.");
            }
        }
	}
    
}
