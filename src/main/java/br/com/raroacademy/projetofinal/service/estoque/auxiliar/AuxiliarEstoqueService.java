package br.com.raroacademy.projetofinal.service.estoque.auxiliar;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.raroacademy.projetofinal.dto.estoque.EstoqueGeralRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.exception.equipamento.tipoEquipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.estoque.EstoqueNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.estoque.EstoqueRepository;
import br.com.raroacademy.projetofinal.repository.usuario.UsuarioRepositorio;
import br.com.raroacademy.projetofinal.service.usuario.EmailServico;

@Component
public class AuxiliarEstoqueService {

	@Autowired
	private EstoqueRepository estoqueRepository;
	
	@Autowired
	private TipoEquipamentoRepository tipoEquipamentoRepository;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private EmailServico emailServico;
	

	public Page<EstoqueGeralRespostaDTO> listarEstoquePorTipoEStatus(String tipoEquipamento, 
					List<STATUS_EQUIPAMENTO> status, Pageable paginacao) {
		
		return estoqueRepository.listarEstoquePorTipoEStatus(tipoEquipamento, status, paginacao);
	}
	
	public Long contarTotalPorTipo(Long tipoId) {
        return estoqueRepository.countByTipoEquipamentoId(tipoId);
    }

    public Long contarPorTipoEStatus(Long tipoId, List<STATUS_EQUIPAMENTO> status) {
        return estoqueRepository.countByTipoEquipamentoIdAndStatusIn(tipoId, status);
    }

    public Map<Long, List<Equipamento>> agruparPorTipo(List<Equipamento> equipamentos) {
        return equipamentos.stream()
            .collect(Collectors.groupingBy(e -> e.getTipoEquipamento().getId()));
    }  
    
    public void validarEstoqueNaoVazio(Page<?> pagina, String mensagemErro) {
        if (pagina.isEmpty()) {
            throw new EstoqueNaoEncontradoException(mensagemErro);
        }
    }
	
	public void validarTipoEquipamento(String nomeTipoEquipamento) {
        boolean existe = tipoEquipamentoRepository.existsByNomeIgnoreCase(nomeTipoEquipamento.trim());
        if (!existe) {
            throw new TipoEquipamentoNaoEncontradoException("Tipo de Equipamento '" + nomeTipoEquipamento + "' não encontrado.");
        }
    }
	
	public void validarTipoEquipamentoObrigatorio(String tipoEquipamento) {
		if (tipoEquipamento == null || tipoEquipamento.trim().isEmpty()) {
            throw new IllegalArgumentException("O parâmetro 'tipoEquipamento' é obrigatório e não pode estar vazio.");
        }
	}

    public String normalizarTipoEquipamento(String tipoEquipamento) {
        return (tipoEquipamento != null && !tipoEquipamento.trim().isEmpty()) ? tipoEquipamento.trim() : null;
    }

	public Page<Equipamento> buscarEstoqueDeEquipamentosDetalhado(String tipoEquipamento, List<STATUS_EQUIPAMENTO> status,
			Pageable paginacao) {
		return estoqueRepository.buscarEquipamentosDetalhado(tipoEquipamento, status, paginacao);
	}
	
	public void verificarEstoquePorTipo(Long tipoEquipamentoId) {
        TipoEquipamento tipoEquipamento = tipoEquipamentoRepository.findById(tipoEquipamentoId).orElseThrow(() -> new IllegalArgumentException("Tipo de equipamento não encontrado"));

        Long totalDisponivel = estoqueRepository.countByTipoEquipamentoIdAndStatusIn(
        		tipoEquipamento.getId(), List.of(STATUS_EQUIPAMENTO.DISPONIVEL)
        );

        verificarEEnviarAlertaEstoqueBaixo(tipoEquipamento.getNome(), totalDisponivel, tipoEquipamento.getEstoqueMinimo());
    }
	
	private void verificarEEnviarAlertaEstoqueBaixo(String tipoEquipamento, Long quantidadeAtual, Integer estoqueMinimo) {
		System.out.println(tipoEquipamento);
		System.out.println(quantidadeAtual);
		System.out.println(estoqueMinimo);
        if (quantidadeAtual <= estoqueMinimo) {
            String assunto = "Alerta de Estoque Baixo";
            String corpo = "<p>O estoque do equipamento <strong>" + tipoEquipamento + "</strong> está abaixo do mínimo necessário.</p>" +
                    "<p><strong>Quantidade atual:</strong> " + quantidadeAtual + "</p>" +
                    "<p><strong>Estoque mínimo:</strong> " + estoqueMinimo + "</p>";

            List<Usuario> usuarios = usuarioRepositorio.findAll();

            for (Usuario usuario : usuarios) {
                try {
                    emailServico.enviarEmail(usuario.getEmail(), assunto, corpo);
                } catch (Exception e) {
                    System.err.println("Erro ao enviar e-mail para: " + usuario.getEmail() + " - " + e.getMessage());
                }
            }
        }
    }
  
}