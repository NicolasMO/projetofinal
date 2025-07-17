package br.com.raroacademy.projetofinal.service.parametro;

import br.com.raroacademy.projetofinal.dto.parametro.ParametroDTO;
import br.com.raroacademy.projetofinal.dto.parametro.TempoEnvioRegiaoDTO;
import br.com.raroacademy.projetofinal.exception.equipamento.tipoEquipamento.TipoEquipamentoNaoEncontradoException;
import br.com.raroacademy.projetofinal.model.equipamento.TipoEquipamento;
import br.com.raroacademy.projetofinal.model.parametro.Parametro;
import br.com.raroacademy.projetofinal.model.parametro.TempoEnvioPorRegiao;
import br.com.raroacademy.projetofinal.repository.equipamento.TipoEquipamentoRepository;
import br.com.raroacademy.projetofinal.repository.parametro.ParametroRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParametroService {

    @Autowired
    private ParametroRepositorio parametroRepositorio;

    @Autowired
    private ParametroRepositorio repositorio;

    @Autowired
    private TipoEquipamentoRepository tipoEquipamentoRepository;


    @Transactional
    public void criar(ParametroDTO dto) {
        Parametro parametro = new Parametro();
        parametro.setTempoMedioReposicaoDias(dto.tempoMedioReposicaoDias());
        parametro.setTempoMedioConsumoEstoqueDias(dto.tempoMedioConsumoEstoqueDias());
        parametro.setTaxaMediaDefeituososPercentual(dto.taxaMediaDefeituososPercentual());

        TipoEquipamento tipoEquipamento = tipoEquipamentoRepository.findById(dto.tipoEquipamentoId())
                .orElseThrow(() -> new TipoEquipamentoNaoEncontradoException(
                        "Tipo de equipamento com ID " + dto.tipoEquipamentoId() + " não foi encontrado."
                ));

        parametro.setTipoEquipamento(tipoEquipamento);

        List<TempoEnvioPorRegiao> tempos = new ArrayList<>();
        for (TempoEnvioRegiaoDTO t : dto.temposEnvio()) {
            TempoEnvioPorRegiao tempo = new TempoEnvioPorRegiao();
            tempo.setRegiao(t.regiao());
            tempo.setTempoEnvioDias(t.tempoEnvioDias());
            tempo.setParametro(parametro);
            tempos.add(tempo);
        }

        parametro.setTemposEnvio(tempos);
        repositorio.save(parametro);
    }

    public List<Parametro> listar() {
        return repositorio.findAll();
    }

    public void atualizar(Long id, ParametroDTO dto) {
        Parametro parametro = repositorio.findById(id).orElseThrow(() -> new IllegalArgumentException("Parâmetro com ID " + id + " não encontrado."));

        parametro.setTempoMedioReposicaoDias(dto.tempoMedioReposicaoDias());
        parametro.setTempoMedioConsumoEstoqueDias(dto.tempoMedioConsumoEstoqueDias());
        parametro.setTaxaMediaDefeituososPercentual(dto.taxaMediaDefeituososPercentual());

        parametro.getTemposEnvio().clear();

        List<TempoEnvioPorRegiao> novosTempos = new ArrayList<>();
        for (TempoEnvioRegiaoDTO t : dto.temposEnvio()) {
            TempoEnvioPorRegiao tempo = new TempoEnvioPorRegiao();
            tempo.setRegiao(t.regiao());
            tempo.setTempoEnvioDias(t.tempoEnvioDias());
            tempo.setParametro(parametro);
            novosTempos.add(tempo);
        }

        parametro.getTemposEnvio().addAll(novosTempos);

        repositorio.save(parametro);
    }

    public void deletar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new IllegalArgumentException("Parâmetro com ID " + id + " não encontrado.");
        }
        repositorio.deleteById(id);
    }

    @Transactional
    public void atualizarTempoEnvioPorRegiao(Long tipoEquipamentoId, String nomeRegiao, int novoTempoEmDias) {
        Parametro parametro = buscarOuCriarParametro(tipoEquipamentoId);
        atualizarOuAdicionarTempoEnvio(parametro, nomeRegiao, novoTempoEmDias);
        parametroRepositorio.save(parametro);
    }

    @Transactional
    public void atualizarTempoConsumoEstoque(Long tipoEquipamentoId, int diasDesdeUltimoEnvio) {
        Parametro parametro = buscarOuCriarParametro(tipoEquipamentoId);

        int mediaAtual = parametro.getTempoMedioConsumoEstoqueDias();
        int novaMedia = mediaAtual == 0 ? diasDesdeUltimoEnvio : (mediaAtual + diasDesdeUltimoEnvio) / 2;

        parametro.setTempoMedioConsumoEstoqueDias(novaMedia);
        parametroRepositorio.save(parametro);
    }


    private Parametro buscarOuCriarParametro(Long tipoEquipamentoId) {
        return parametroRepositorio.findByTipoEquipamentoId(tipoEquipamentoId).orElseGet(() -> criarNovoParametro(tipoEquipamentoId));
    }

    private Parametro criarNovoParametro(Long tipoEquipamentoId) {
        TipoEquipamento tipoEquipamento = tipoEquipamentoRepository.findById(tipoEquipamentoId).orElseThrow(() -> new RuntimeException("Tipo de equipamento não encontrado: " + tipoEquipamentoId));

        Parametro parametro = new Parametro();
        parametro.setTempoMedioConsumoEstoqueDias(0);
        parametro.setTempoMedioReposicaoDias(0);
        parametro.setTaxaMediaDefeituososPercentual(0.0);
        parametro.setTipoEquipamento(tipoEquipamento);
        parametro.setTemposEnvio(new ArrayList<>());

        return parametroRepositorio.save(parametro);
    }

    private void atualizarOuAdicionarTempoEnvio(Parametro parametro, String nomeRegiao, int novoTempoEmDias) {
        parametro.getTemposEnvio().stream()
                .filter(tempo -> tempo.getRegiao().equalsIgnoreCase(nomeRegiao))
                .findFirst()
                .ifPresentOrElse(
                        tempoExistente -> {
                            int media = (tempoExistente.getTempoEnvioDias() + novoTempoEmDias) / 2;
                            tempoExistente.setTempoEnvioDias(media);
                        },
                        () -> {
                            TempoEnvioPorRegiao novoTempo = new TempoEnvioPorRegiao();
                            novoTempo.setRegiao(nomeRegiao);
                            novoTempo.setTempoEnvioDias(novoTempoEmDias);
                            novoTempo.setParametro(parametro);
                            parametro.getTemposEnvio().add(novoTempo);
                        }
                );
    }
}
