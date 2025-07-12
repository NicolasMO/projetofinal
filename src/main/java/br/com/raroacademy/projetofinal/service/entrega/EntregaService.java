package br.com.raroacademy.projetofinal.service.entrega;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.entrega.EntregaRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.entrega.EntregaRespostaDTO;
import br.com.raroacademy.projetofinal.dto.entrega.EquipamentoEntregaDTO;
import br.com.raroacademy.projetofinal.dto.entrega.EquipamentoEntregaEspecificacaoDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_ENTREGA;
import br.com.raroacademy.projetofinal.exception.equipamento.EspecificacaoNaoEncontradaException;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.entregas.Entrega;
import br.com.raroacademy.projetofinal.model.entregas.EquipamentoEntrega;
import br.com.raroacademy.projetofinal.model.entregas.EquipamentoEntregaEspecificacao;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.repository.entrega.EntregaRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.EspecificacaoRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;

@Service
public class EntregaService {
	
	@Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;
    
    @Autowired
    private EspecificacaoRepository especificacaoRepository;
    
    public void solicitarEntrega(EntregaRequisicaoDTO dto) {
    	Entrega entrega = new Entrega();
    	entrega.setDataSolicitacao(dto.dataSolicitacao());
    	entrega.setStatus(STATUS_ENTREGA.SOLICITADO);
    	entrega.setDataPrevisaoEntrega(dto.dataSolicitacao().plusDays(7));
    	
    	List<EquipamentoEntrega> equipamentos = new ArrayList<>();
    	
    	for (EquipamentoEntregaDTO equipDTO : dto.equipamentos()) {
    		TipoEquipamento tipoEquipamento = tipoEquipamentoRepository
    				.findByNomeIgnoreCase(equipDTO.tipoEquipamento())
    				.orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(null));
    		
    		EquipamentoEntrega equipamentoEntrega = new EquipamentoEntrega();
            equipamentoEntrega.setEntrega(entrega);
            equipamentoEntrega.setModelo(equipDTO.modelo());
            equipamentoEntrega.setMarca(equipDTO.marca());
            equipamentoEntrega.setTipoEquipamento(tipoEquipamento);
            equipamentoEntrega.setEntregue(false);
            
            List<EquipamentoEntregaEspecificacao> especificacoes = equipDTO.especificacoes().stream()
            	    .map(especDTO -> {
            	        Especificacao especExistente = especificacaoRepository
            	            .findByDescricaoIgnoreCaseAndValorIgnoreCase(especDTO.descricao(), especDTO.valor())
            	            .orElseThrow(() -> new EspecificacaoNaoEncontradaException("Especificação '" + especDTO.descricao() + ": " + especDTO.valor() + "' não encontrada."));

            	        EquipamentoEntregaEspecificacao espec = new EquipamentoEntregaEspecificacao();
            	        espec.setDescricao(especExistente.getDescricao());
            	        espec.setValor(especExistente.getValor());
            	        espec.setEquipamentoEntrega(equipamentoEntrega);
            	        return espec;
            	    }).toList();

            equipamentoEntrega.setEspecificacoes(especificacoes);

            equipamentos.add(equipamentoEntrega);
    	}
    	
    	entrega.setEquipamentos(equipamentos);

        entregaRepository.save(entrega);
    }
    
    public List<EntregaRespostaDTO> listarTodasEntregas() {
        List<Entrega> entregas = entregaRepository.findAll();

        return entregas.stream().map(entrega -> new EntregaRespostaDTO(
                entrega.getId(),
                entrega.getDataSolicitacao(),
                entrega.getDataPrevisaoEntrega(),
                entrega.getDataEntrega(),
                entrega.getStatus().name(),
                entrega.getEquipamentos().stream().map(equip -> new EquipamentoEntregaDTO(
                        equip.getModelo(),
                        equip.getMarca(),
                        equip.getTipoEquipamento().getNome(),
                        equip.isEntregue(),
                        equip.getEspecificacoes().stream().map(espec -> new EquipamentoEntregaEspecificacaoDTO(
                                espec.getDescricao(),
                                espec.getValor()
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }
}
