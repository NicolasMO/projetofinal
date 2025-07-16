package br.com.raroacademy.projetofinal.service.estoque;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoPaginaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueGeralRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.exception.estoque.EstoqueNaoEncontradoException;
import br.com.raroacademy.projetofinal.mapper.estoque.EstoqueMapper;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.estoque.EstoqueRepository;
import br.com.raroacademy.projetofinal.repository.usuario.UsuarioRepositorio;
import br.com.raroacademy.projetofinal.service.usuario.EmailServico;

@Service
public class EstoqueService {

	@Autowired
	private EstoqueRepository estoqueRepository;
	
	@Autowired
	private TipoEquipamentoRepository tipoEquipamentoRepository;

    @Autowired
    private EmailServico emailServico;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private EstoqueMapper estoqueMapper;

    public Page<EstoqueGeralRespostaDTO> obterEstoqueGeral(String tipoEquipamento, List<STATUS_EQUIPAMENTO> status, Pageable paginacao) {
        if (tipoEquipamento != null && !tipoEquipamento.trim().isEmpty()) {
        	validarTipoEquipamento(tipoEquipamento);
        }

        Page<EstoqueGeralRespostaDTO> resultado = estoqueRepository.listarEstoquePorTipoEStatus(
                		(tipoEquipamento != null && !tipoEquipamento.trim().isEmpty()) ? tipoEquipamento.trim() : null,
                				status, paginacao
                    	);

        if (resultado.isEmpty()) {
            throw new EstoqueNaoEncontradoException("Nenhum equipamento encontrado para os filtros informados.");
        }

        return resultado;
    }
    
    public EstoqueDetalhadoPaginaDTO obterEstoqueDetalhado(String tipoEquipamento, List<STATUS_EQUIPAMENTO> status, Pageable paginacao) {
        validarTipoEquipamentoObrigatorio(tipoEquipamento);

        Page<Equipamento> estoquePagina = estoqueRepository.buscarEquipamentosDetalhado(tipoEquipamento, status, paginacao);
        
        if (estoquePagina.isEmpty()) {
            throw new EstoqueNaoEncontradoException("Nenhum equipamento encontrado para os filtros informados.");
        }

        Map<Long, List<Equipamento>> equipamentosPorTipo = estoquePagina.getContent().stream()
            .collect(Collectors.groupingBy(e -> e.getTipoEquipamento().getId()));

        List<EstoqueDetalhadoRespostaDTO> estoqueEquipamentos = equipamentosPorTipo.values().stream()
            .map(equipamentos -> {
                Long tipoId = equipamentos.get(0).getTipoEquipamento().getId();
                Long total = estoqueRepository.countByTipoEquipamentoId(tipoId);
                Long totalStatus = (status != null && !status.isEmpty())
                        ? estoqueRepository.countByTipoEquipamentoIdAndStatusIn(tipoId, status)
                        : null;

                return estoqueMapper.paraDetalhadoRespostaDTO(equipamentos, status, total, totalStatus);
            })
            .collect(Collectors.toList());

        return new EstoqueDetalhadoPaginaDTO(
            estoqueEquipamentos,
            estoquePagina.getNumber(),
            estoquePagina.getTotalPages(),
            estoquePagina.getTotalElements(),
            estoquePagina.getSize(),
            estoquePagina.isLast()
        );
    }

	

    private void validarTipoEquipamento(String tipoEquipamento) {
		boolean existe = tipoEquipamentoRepository.existsByNomeIgnoreCase(tipoEquipamento.trim());
		if (!existe) {
		    throw new TipoEquipamentoNaoEncontradoException("Tipo de Equipamento '" + tipoEquipamento + "' não encontrado.");
		}
	}
    
    private void validarTipoEquipamentoObrigatorio(String tipoEquipamento) {
		if (tipoEquipamento == null || tipoEquipamento.trim().isEmpty()) {
            throw new IllegalArgumentException("O parâmetro 'tipoEquipamento' é obrigatório e não pode estar vazio.");
        }

        validarTipoEquipamento(tipoEquipamento);
	}
    
    private void verificarEEnviarAlertaEstoqueBaixo(String tipoEquipamento, Long quantidadeAtual, Integer estoqueMinimo) {
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

    public void verificarEstoquePorTipo(Long tipoEquipamentoId) {
        TipoEquipamento tipo = tipoEquipamentoRepository.findById(tipoEquipamentoId).orElseThrow(() -> new IllegalArgumentException("Tipo de equipamento não encontrado"));

        Long totalDisponivel = estoqueRepository.countByTipoEquipamentoIdAndStatusIn(
                tipo.getId(), List.of(STATUS_EQUIPAMENTO.DISPONIVEL)
        );

        verificarEEnviarAlertaEstoqueBaixo(tipo.getNome(), totalDisponivel, tipo.getEstoqueMinimo());
    }
}
