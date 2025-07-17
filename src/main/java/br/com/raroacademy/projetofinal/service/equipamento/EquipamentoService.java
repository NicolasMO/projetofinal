package br.com.raroacademy.projetofinal.service.equipamento;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoAtualizarDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.mapper.equipamento.MapeadorDeEquipamentos;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.service.equipamento.auxiliar.AuxiliarEquipamentoService;
import br.com.raroacademy.projetofinal.service.estoque.auxiliar.AuxiliarEstoqueService;

@Service
public class EquipamentoService {

    @Autowired
	private AuxiliarEquipamentoService auxiliarEquipamentoService;
    
    @Autowired
    private AuxiliarEstoqueService auxiliarEstoqueService;


    public EquipamentoRespostaDTO criar(EquipamentoRequisicaoDTO dto) {
    	auxiliarEquipamentoService.validarNumeroSerieNaoExistente(dto.numeroSerie());
    	
    	TipoEquipamento tipoEquipamento = auxiliarEquipamentoService.buscarTipoPorId(dto.tipoEquipamentoId());
        Set<Especificacao> especificacoes = auxiliarEquipamentoService.buscarEspecificacoesValidas(dto.especificacoesIds());

        Equipamento novoEquipamento = MapeadorDeEquipamentos.paraEntidadeEquipamento(dto, tipoEquipamento, especificacoes);
        auxiliarEquipamentoService.salvarEquipamento(novoEquipamento);
        auxiliarEstoqueService.verificarEstoquePorTipo(tipoEquipamento.getId());
        
        return MapeadorDeEquipamentos.paraEquipamentoRespostaDTO(novoEquipamento);
    }

    public EquipamentoRespostaDTO buscarPorNumeroSerie(String numeroSerie) {
        return MapeadorDeEquipamentos.paraEquipamentoRespostaDTO(auxiliarEquipamentoService.buscarEquipamentoPorNumSerie(numeroSerie));
    }

    public Page<EquipamentoRespostaDTO> listarTodos(Pageable paginacao) {
    	return auxiliarEquipamentoService.listarTodos(paginacao)
    	        .map(MapeadorDeEquipamentos::paraEquipamentoRespostaDTO);
    }
    
    public EquipamentoRespostaDTO atualizarEquipamento(String numeroSerie, EquipamentoAtualizarDTO dto) {
    	Equipamento equipamento = auxiliarEquipamentoService.buscarEquipamentoPorNumSerie(numeroSerie);
        TipoEquipamento tipoEquipamento = auxiliarEquipamentoService.buscarTipoPorId(dto.tipoEquipamentoId());
        Set<Especificacao> especificacoes = auxiliarEquipamentoService.buscarEspecificacoesValidas(dto.especificacoesIds());

        equipamento.atualizarComDTO(dto, tipoEquipamento, especificacoes);
        
        return MapeadorDeEquipamentos.paraEquipamentoRespostaDTO(equipamento);
    }
    
    public void deletarEquipamento(String numeroSerie) {
    	auxiliarEquipamentoService.deletarEquipamento(numeroSerie);
	}    
}