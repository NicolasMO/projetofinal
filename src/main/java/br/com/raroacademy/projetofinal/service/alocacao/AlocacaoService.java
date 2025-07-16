package br.com.raroacademy.projetofinal.service.alocacao;

import br.com.raroacademy.projetofinal.dto.equipamento.EquipamentoAtualizarDTO;
import br.com.raroacademy.projetofinal.dto.alocacao.*;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.exception.equipamento.EquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.alocacao.AlocacaoEquipamento;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import br.com.raroacademy.projetofinal.model.equipamento.Equipamento;
import br.com.raroacademy.projetofinal.model.equipamento.Especificacao;
import br.com.raroacademy.projetofinal.model.alocacao.Alocacao;
import br.com.raroacademy.projetofinal.repository.colaborador.ColaboradorRepository;
import br.com.raroacademy.projetofinal.repository.equipamento.EquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.alocacao.AlocacaoRepository;
import br.com.raroacademy.projetofinal.repository.alocacao.AlocacaoEquipamentoRepository;
import br.com.raroacademy.projetofinal.service.equipamento.EquipamentoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlocacaoService {

    @Autowired
    private AlocacaoRepository alocacaoRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private AlocacaoEquipamentoRepository alocacaoEquipamentoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EquipamentoService equipamentoService;

    public void cadastraPorColaboradorID(AlocacaoRequisicaoDTO dto) {
    	
        Equipamento equipamento = equipamentoRepository.findById(dto.numeroSerie())
                .orElseThrow(() -> new EquipamentoNaoEncontradoException("Equipamento não encontrado"));

        Colaborador colaborador = colaboradorRepository.findById(dto.colaboradorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador não encontrado"));

        EquipamentoAtualizarDTO atualizarDTO = new EquipamentoAtualizarDTO(
                equipamento.getModelo(),
                equipamento.getMarca(),
                equipamento.getTipoEquipamento().getId(),
                equipamento.getEspecificacoes().stream().map(Especificacao::getId).toList()
        );

        equipamentoService.atualizar(equipamento.getNumeroSerie(), atualizarDTO, STATUS_EQUIPAMENTO.ALOCADO);

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
}
