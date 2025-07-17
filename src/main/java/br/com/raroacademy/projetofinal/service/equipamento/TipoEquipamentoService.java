package br.com.raroacademy.projetofinal.service.equipamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento.AtualizarTipoEquipamentoEstoqueMinimoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento.TipoEquipamentoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.tipoEquipamento.TipoEquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.mapper.equipamento.MapeadorDeTiposEquipamentos;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.service.equipamento.auxiliar.AuxiliarTipoEquipamentoService;

@Service
public class TipoEquipamentoService {
    
    @Autowired
	private AuxiliarTipoEquipamentoService auxiliarTipoEquipamentoService;

    public TipoEquipamentoRespostaDTO criar(TipoEquipamentoRequisicaoDTO dto) {
    	auxiliarTipoEquipamentoService.validarNomeDuplicado(dto.nome());
    	
        TipoEquipamento tipoEquipamento = new TipoEquipamento(dto.nome(), dto.tempoConfiguracao(), dto.estoqueMinimo());
        auxiliarTipoEquipamentoService.salvarTipoEquipamento(tipoEquipamento);
        
        return MapeadorDeTiposEquipamentos.paraTipoEquipamentoRespostaDTO(tipoEquipamento);
    }

    public TipoEquipamentoRespostaDTO buscarPorId(Long id) {
        return MapeadorDeTiposEquipamentos.paraTipoEquipamentoRespostaDTO(auxiliarTipoEquipamentoService.buscarTipoPorId(id));
    }

    public Page<TipoEquipamentoRespostaDTO> listarTodos(Pageable paginacao) {
        return auxiliarTipoEquipamentoService.listarTodos(paginacao)
        		.map(MapeadorDeTiposEquipamentos::paraTipoEquipamentoRespostaDTO);
    }
    
    public TipoEquipamentoRespostaDTO atualizarTipoEquipamento(Long id, TipoEquipamentoRequisicaoDTO dto) {
        TipoEquipamento tipoEquipamento = auxiliarTipoEquipamentoService.buscarTipoPorId(id);
        tipoEquipamento.atualizarDados(dto.nome(), dto.tempoConfiguracao(), dto.estoqueMinimo());
        auxiliarTipoEquipamentoService.salvarTipoEquipamento(tipoEquipamento);
        
        return MapeadorDeTiposEquipamentos.paraTipoEquipamentoRespostaDTO(tipoEquipamento);
    }
    
    public TipoEquipamentoRespostaDTO atualizarEstoqueMinimo(Long id, AtualizarTipoEquipamentoEstoqueMinimoDTO dto) {
        TipoEquipamento tipoEquipamento = auxiliarTipoEquipamentoService.buscarTipoPorId(id);
        tipoEquipamento.setEstoqueMinimo(dto.estoqueMinimo());
        auxiliarTipoEquipamentoService.salvarTipoEquipamento(tipoEquipamento);
        
        return MapeadorDeTiposEquipamentos.paraTipoEquipamentoRespostaDTO(tipoEquipamento);
    }

    public void deletar(Long id) {
        TipoEquipamento tipoEquipamento = auxiliarTipoEquipamentoService.buscarTipoPorId(id);
        auxiliarTipoEquipamentoService.deletarTipoEquipamento(tipoEquipamento);
    }
    
}