package br.com.raroacademy.projetofinal.service.alocacao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRecebimentoDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRelatorioDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.alocacaoEquipamento.AlocacaoEquipamentoResumoProjection;
import br.com.raroacademy.projetofinal.mapper.alocacao.AlocacaoEquipamentoMapper;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.alocacao.AlocacaoEquipamento;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.alocacao.AlocacaoEquipamentoRepository;
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
    
    public AlocacaoEquipamentoRelatorioDTO gerarRelatorio(LocalDate inicio, LocalDate fim, Pageable paginacao) {
        AlocacaoEquipamentoResumoProjection resumo = alocacaoEquipamentoRepository.buscarResumo(inicio, fim);

        Page<AlocacaoEquipamento> alocacaoEquipamentoPaginado = alocacaoEquipamentoRepository.findByDataDevolucaoPrevistaBetween(inicio, fim, paginacao);

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
    
    @Transactional
    public void confirmarRecebimentoEquipamento(AlocacaoEquipamentoRecebimentoDTO dto) {
    
    	AlocacaoEquipamento alocacaoEquipamento = alocacaoEquipamentoRepository.findById(dto.alocacaoEquipamentoId())
    	        .orElseThrow(() -> new IllegalArgumentException("Alocação não encontrada."));
    	
    	if(alocacaoEquipamento.isDevolvido()) {
    		throw new IllegalArgumentException("Equipamento já devolvido.");
    	}
    	
    	String numeroSerieAlocado = alocacaoEquipamento.getEquipamento().getNumeroSerie();

        if (!numeroSerieAlocado.equals(dto.numeroSerie())) {
            throw new IllegalArgumentException("Número de série informado não corresponde ao equipamento alocado.");
        }
        
        Equipamento equipamento = equipamentoRepository.findById(alocacaoEquipamento.getEquipamento().getNumeroSerie())
            .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado."));

        equipamento.setStatus(dto.statusEquipamento());
        equipamentoRepository.save(equipamento);

        alocacaoEquipamento.setStatusEquipamento(dto.statusEquipamento());
        alocacaoEquipamento.setObservacao(dto.observacao());
        alocacaoEquipamento.setDataDevolucao(LocalDate.now());
        alocacaoEquipamento.setDevolvido(true);

        alocacaoEquipamentoRepository.save(alocacaoEquipamento);
    }
    
    public AlocacaoEquipamentoRelatorioDTO buscarEquipamentosPorColaborador(
    		Long colaboradorId,
    		LocalDate inicio, 
    		LocalDate fim,
    		Pageable paginacao
    		) 
    {
    	 Page<AlocacaoEquipamento> page;

	    if (inicio != null && fim != null) {
	    	page = alocacaoEquipamentoRepository.findByColaboradorIdAndDataDevolucaoPrevistaBetween(colaboradorId, inicio, fim, paginacao);
	    } else {
	        page = alocacaoEquipamentoRepository.findByColaboradorId(colaboradorId, paginacao);
	    }

	    long totalPrevisto = page.getTotalElements();
	    long totalDevolvido = page.getContent().stream().filter(AlocacaoEquipamento::isDevolvido).count();
	    long totalPendente = totalPrevisto - totalDevolvido;

	    List<AlocacaoEquipamentoRespostaDTO> detalhes = page.getContent().stream()
	        .map(alocacaoEquipamentoMapper::paraRespostaDTO)
	        .toList();
	    
        return new AlocacaoEquipamentoRelatorioDTO(
        		totalPrevisto,
                totalDevolvido,
                totalPendente,
                page.getNumber(),
                page.getTotalPages() - 1,
                page.getTotalElements(),
                page.getSize(),
                detalhes
        );
    }
  
}
