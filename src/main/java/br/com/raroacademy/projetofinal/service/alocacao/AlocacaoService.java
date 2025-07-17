package br.com.raroacademy.projetofinal.service.alocacao;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.raroacademy.projetofinal.dto.alocacao.AlocacaoAtualizarDatasDTO;
import br.com.raroacademy.projetofinal.dto.alocacao.AlocacaoRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.alocacao.AlocacaoRespostaDTO;
import br.com.raroacademy.projetofinal.dto.equipamento.equipamento.EquipamentoAtualizarDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.exception.equipamento.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.alocacao.Alocacao;
import br.com.raroacademy.projetofinal.model.alocacao.AlocacaoEquipamento;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.repository.alocacao.AlocacaoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.alocacao.AlocacaoRepository;
import br.com.raroacademy.projetofinal.repository.colaborador.ColaboradorRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import br.com.raroacademy.projetofinal.service.equipamento.EquipamentoService;
import br.com.raroacademy.projetofinal.service.parametro.ParametroService;

@Service
public class AlocacaoService {

    @Autowired
    private AlocacaoRepository alocacaoRepository;

    @Autowired
    private ParametroService parametroService;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private AlocacaoEquipamentoRepository alocacaoEquipamentoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EquipamentoService equipamentoService;

    public void cadastrar(AlocacaoRequisicaoDTO dto) {

        Colaborador colaborador = new Colaborador();

        Equipamento equipamento = equipamentoRepository.findById(dto.numeroSerie())
                .orElseThrow(() -> new EquipamentoNaoEncontradoException("Equipamento não encontrado"));

        if (equipamento.getStatus() == STATUS_EQUIPAMENTO.ALOCADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipamento já alocado");
        }

        if (dto.colaboradorId() != null) {
            colaborador = colaboradorRepository.findById(dto.colaboradorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador não encontrado"));
        } else if (dto.colaboradorNome() != null) {
            colaborador = colaboradorRepository.findByNome(dto.colaboradorNome());
        }

        if (dto.colaboradorId() == null && dto.colaboradorNome() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos de ID e nome de colaborador não informados");
        }

        EquipamentoAtualizarDTO atualizarDTO = new EquipamentoAtualizarDTO(
                equipamento.getModelo(),
                equipamento.getMarca(),
                equipamento.getTipoEquipamento().getId(),
                STATUS_EQUIPAMENTO.ALOCADO,
                equipamento.getEspecificacoes().stream().map(Especificacao::getId).toList()
        );

        equipamentoService.atualizarEquipamento(equipamento.getNumeroSerie(), atualizarDTO);

        Alocacao alocacao = new Alocacao(
                equipamento,
                colaborador,
                dto.dataEnvio(),
                dto.dataEntrega(),
                dto.dataEntregaPrevista(),
                null,
                dto.dataDevolucaoPrevista()
        );

        alocacaoRepository.save(alocacao);
        atualizarTempoEnvioSePossivel(alocacao);
        atualizarTempoConsumoSePossivel(alocacao);

        AlocacaoEquipamento alocacaoEquipamento = new AlocacaoEquipamento();
        alocacaoEquipamento.setEquipamento(equipamento);
        alocacaoEquipamento.setColaborador(colaborador);
        alocacaoEquipamento.setDataEntregaPrevista(dto.dataEntregaPrevista());
        alocacaoEquipamento.setDataDevolucaoPrevista(dto.dataDevolucaoPrevista());
        alocacaoEquipamento.setDevolvido(false);
        alocacaoEquipamento.setStatusEquipamento(STATUS_EQUIPAMENTO.ALOCADO);
        alocacaoEquipamento.setObservacao("Registro gerado a partir da movimentação");

        alocacaoEquipamentoRepository.save(alocacaoEquipamento);
    }

    public void atualizaDataEnvio(Long id, AlocacaoAtualizarDatasDTO dto) {
        Alocacao alocacao = alocacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alocação não encontrada"));

        alocacao.setDataEnvio(dto.data());

        alocacaoRepository.save(alocacao);
    }

    public void atualizaDataEntrega(Long id, AlocacaoAtualizarDatasDTO dto) {
        Alocacao alocacao = alocacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alocação não encontrada"));

        alocacao.setDataEntrega(dto.data());

        alocacaoRepository.save(alocacao);
        atualizarTempoEnvioSePossivel(alocacao);
        atualizarTempoConsumoSePossivel(alocacao);
    }

    public void atualizaDataDevolucao(Long id, AlocacaoAtualizarDatasDTO dto) {
        Alocacao alocacao = alocacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alocação não encontrada"));

        alocacao.setDataDevolucao(dto.data());

        alocacaoRepository.save(alocacao);
    }

    public Page<AlocacaoRespostaDTO> listarTodas(Pageable paginacao) {
        return alocacaoRepository.findAll(paginacao)
                .map(mov -> new AlocacaoRespostaDTO(
                        mov.getId(),
                        mov.getEquipamento().getNumeroSerie(),
                        mov.getColaborador().getId(),
                        mov.getDataEnvio(),
                        mov.getDataEntrega(),
                        mov.getDataEntregaPrevista(),
                        mov.getDataDevolucao(),
                        mov.getDataDevolucaoPrevista()
                ));
    }

    public AlocacaoRespostaDTO buscarPorId(Long id) {
        Alocacao alocacao = buscaAlocacaoPorId(id);

        return new AlocacaoRespostaDTO(
                alocacao.getId(),
                alocacao.getEquipamento().getNumeroSerie(),
                alocacao.getColaborador().getId(),
                alocacao.getDataEnvio(),
                alocacao.getDataEntrega(),
                alocacao.getDataEntregaPrevista(),
                alocacao.getDataDevolucao(),
                alocacao.getDataDevolucaoPrevista()
        );
    }

    public Alocacao buscaAlocacaoPorId(Long id) {
        return alocacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alocação não encontrada"));
    }

    public void deletar(Long id) {
        if(!alocacaoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alocação não encontrada");
        }
        alocacaoRepository.deleteById(id);
    }

    private void atualizarTempoEnvioSePossivel(Alocacao alocacao) {
        if (alocacao.getDataEnvio() != null && alocacao.getDataEntrega() != null) {
            long diasEnvio = ChronoUnit.DAYS.between(
                    alocacao.getDataEnvio(),
                    alocacao.getDataEntrega()
            );

            String regiao = alocacao.getColaborador().getEndereco().getRegiao();
            Long tipoEquipamentoId = alocacao.getEquipamento().getTipoEquipamento().getId();

            parametroService.atualizarTempoEnvioPorRegiao(tipoEquipamentoId, regiao, (int) diasEnvio);
        }
    }

    private void atualizarTempoConsumoSePossivel(Alocacao alocacao) {
        if (alocacao.getDataEnvio() == null) return;

        Long tipoEquipamentoId = alocacao.getEquipamento().getTipoEquipamento().getId();
        List<Alocacao> alocacoesComEnvio = alocacaoRepository.buscarTodosEnviosPorTipo(tipoEquipamentoId);

        if (alocacoesComEnvio.size() < 2) return;

        long somaDias = 0;
        int totalIntervalosValidos = 0;

        for (int i = 1; i < alocacoesComEnvio.size(); i++) {
            var envioAnterior = alocacoesComEnvio.get(i - 1).getDataEnvio();
            var envioAtual = alocacoesComEnvio.get(i).getDataEnvio();
            long intervalo = ChronoUnit.DAYS.between(envioAnterior, envioAtual);

            if (intervalo > 0) {
                somaDias += intervalo;
                totalIntervalosValidos++;
            }
        }

        if (totalIntervalosValidos > 0) {
            int mediaDias = (int) (somaDias / totalIntervalosValidos);
            parametroService.atualizarTempoConsumoEstoque(tipoEquipamentoId, mediaDias);
        }
    }
}
