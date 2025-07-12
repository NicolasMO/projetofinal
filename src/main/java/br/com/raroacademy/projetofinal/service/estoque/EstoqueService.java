package br.com.raroacademy.projetofinal.service.estoque;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.raroacademy.projetofinal.model.usuario.Usuario;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.usuario.UsuarioRepositorio;
import br.com.raroacademy.projetofinal.service.usuario.EmailServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.EspecificacaoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoPaginaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueGeralRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.exception.equipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.estoque.EstoqueRepository;

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

    public List<EstoqueGeralRespostaDTO> obterEstoqueGeral(String tipoEquipamento, List<STATUS_EQUIPAMENTO> status) {
        return estoqueRepository.listarEstoquePorTipoEStatus(tipoEquipamento, status);
    }
    
    public EstoqueDetalhadoPaginaDTO obterEstoqueDetalhado(String tipoEquipamento, List<STATUS_EQUIPAMENTO> status, Pageable paginacao) {
    	boolean tipoVazio = (tipoEquipamento == null || tipoEquipamento.trim().isEmpty());
	    boolean statusVazio = (status == null || status.isEmpty());

	    if (tipoVazio) {
            throw new IllegalArgumentException("O parâmetro 'tipoEquipamento' é obrigatório e não pode estar vazio.");
        }

        // Verifica se o tipoEquipamento existe no banco
        boolean tipoExiste = tipoEquipamentoRepository.existsByNomeIgnoreCase(tipoEquipamento);
        if (!tipoExiste) {
            throw new TipoEquipamentoNaoEncontradoException("Tipo de Equipamento " + tipoEquipamento + " não encontrado.");
        }

        Page<Equipamento> estoquePagina = estoqueRepository.buscarEquipamentosDetalhado(tipoEquipamento, status, paginacao);

        Map<Long, List<Equipamento>> equipamentosPorTipo = estoquePagina.getContent().stream()
            .collect(Collectors.groupingBy(e -> e.getTipoEquipamento().getId()));

        List<EstoqueDetalhadoRespostaDTO> estoqueEquipamentos = equipamentosPorTipo.values().stream()
            .map(equipamento -> mapearParaDTO(equipamento, status))
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
    
    private EstoqueDetalhadoRespostaDTO mapearParaDTO(List<Equipamento> equipamentosDoTipo, List<STATUS_EQUIPAMENTO> status) {
        if (equipamentosDoTipo == null || equipamentosDoTipo.isEmpty()) {
            throw new IllegalArgumentException("Lista de equipamentos vazia.");
        }

        TipoEquipamento tipo = equipamentosDoTipo.get(0).getTipoEquipamento();

        List<EquipamentoRespostaDTO> equipamentosDto = equipamentosDoTipo.stream()
            .map(equip -> new EquipamentoRespostaDTO(
                equip.getNumeroSerie(),
                equip.getModelo(),
                equip.getMarca(),
                equip.getDataAquisicao(),
                equip.getTempoUso(),
                equip.getStatus(),
                tipo.getNome(),
                equip.getEspecificacoes().stream()
                    .map(espec -> new EspecificacaoRespostaDTO(espec.getId(), espec.getDescricao(), espec.getValor()))
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
        
        Long estoqueTotal = estoqueRepository.countByTipoEquipamentoId(tipo.getId());

        Long estoqueTotalStatus = null;
        if (status != null && !status.isEmpty()) {
        	estoqueTotalStatus = estoqueRepository.countByTipoEquipamentoIdAndStatusIn(tipo.getId(), status);
        }

        return new EstoqueDetalhadoRespostaDTO(
            tipo.getId(),
            tipo.getNome(),
            tipo.getEstoqueMinimo(),
            estoqueTotal,
            estoqueTotalStatus,
            equipamentosDto
        );
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
