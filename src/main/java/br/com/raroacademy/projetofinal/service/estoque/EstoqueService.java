package br.com.raroacademy.projetofinal.service.estoque;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoPaginaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueGeralRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.mapper.estoque.MapeadorDeEstoque;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.service.estoque.auxiliar.AuxiliarEstoqueService;

@Service
public class EstoqueService {

	
	@Autowired
	private AuxiliarEstoqueService auxiliarEstoqueService;

    public Page<EstoqueGeralRespostaDTO> obterEstoqueGeral(String tipoEquipamento, List<STATUS_EQUIPAMENTO> status, Pageable paginacao) {
    	String tipoEquipamentoNormalizado = auxiliarEstoqueService.normalizarTipoEquipamento(tipoEquipamento);
    	
    	if (tipoEquipamento != null && !tipoEquipamento.trim().isEmpty()) {
    		auxiliarEstoqueService.validarTipoEquipamento(tipoEquipamento);
        }

    	Page<EstoqueGeralRespostaDTO> estoqueEquipamentos = auxiliarEstoqueService.listarEstoquePorTipoEStatus(tipoEquipamentoNormalizado, status, paginacao);

    	auxiliarEstoqueService.validarEstoqueNaoVazio(estoqueEquipamentos, "Nenhum equipamento encontrado para os filtros informados.");
    	
        return estoqueEquipamentos;
    }
    
    public EstoqueDetalhadoPaginaDTO obterEstoqueDetalhado(String tipoEquipamento, List<STATUS_EQUIPAMENTO> status, Pageable paginacao) {
        auxiliarEstoqueService.validarTipoEquipamentoObrigatorio(tipoEquipamento);

        Page<Equipamento> estoquePagina = auxiliarEstoqueService.buscarEstoqueDeEquipamentosDetalhado(tipoEquipamento, status, paginacao);
        
        auxiliarEstoqueService.validarEstoqueNaoVazio(estoquePagina, "Nenhum equipamento encontrado para os filtros informados.");

        Map<Long, List<Equipamento>> agrupadoPorTipo = auxiliarEstoqueService.agruparPorTipo(estoquePagina.getContent());
        List<EstoqueDetalhadoRespostaDTO> estoqueEquipamentos = MapeadorDeEstoque.mapearPaginaEstoqueDetalhado(agrupadoPorTipo, status);
        
        return MapeadorDeEstoque.mapearParaPaginaDTO(estoqueEquipamentos, estoquePagina.getNumber(),
        				estoquePagina.getTotalPages(), estoquePagina.getTotalElements(),
        				estoquePagina.getSize(), estoquePagina.isLast()
        				);   
    }
  
}
