package br.com.raroacademy.projetofinal.service.colaborador;

import br.com.raroacademy.projetofinal.client.ViaCepClient;
import br.com.raroacademy.projetofinal.dto.colaborador.*;
import br.com.raroacademy.projetofinal.model.colaborador.Colaborador;
import br.com.raroacademy.projetofinal.model.colaborador.EnderecoColaborador;
import br.com.raroacademy.projetofinal.repository.colaborador.ColaboradorRepository;
import br.com.raroacademy.projetofinal.repository.colaborador.EnderecoColaboradorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EnderecoColaboradorRepository enderecoColaboradorRepository;

    @Autowired
    private ViaCepClient viaCepClient;

    @PersistenceContext
    private EntityManager entityManager;

    private String validarECepLimpo(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório.");
        }
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException("CEP inválido. Deve conter exatamente 8 dígitos numéricos.");
        }
        return cepLimpo;
    }

    private void validarEnderecoViaCep(EnderecoRequisicaoDTO endereco) {
        if (endereco == null ||
                endereco.logradouro() == null || endereco.logradouro().isBlank() ||
                endereco.uf() == null || endereco.uf().isBlank() ||
                endereco.estado() == null || endereco.estado().isBlank()) {
            throw new IllegalArgumentException("CEP não encontrado ou inválido na API ViaCep.");
        }
    }


    @Transactional
    public void salvarColaborador(ColaboradorRequisicaoDTO reqDTO) {

        String cepLimpo = validarECepLimpo(reqDTO.endereco().cep());

        EnderecoRequisicaoDTO enderecoViaCep = viaCepClient.getEndereco(cepLimpo);

        validarEnderecoViaCep(enderecoViaCep);

        Colaborador colaborador = new Colaborador();
        colaborador.setNome(reqDTO.nome());
        colaborador.setEmail(reqDTO.email());
        colaborador.setCpf(reqDTO.cpf());
        colaborador.setCargo(reqDTO.cargo());
        colaborador.setTelefone(reqDTO.telefone());
        colaborador.setData_admissao(reqDTO.data_admissao());
        colaborador.setData_demissao(reqDTO.data_demissao());
        colaborador.setStatusAtivo(true);

        EnderecoColaborador endereco = new EnderecoColaborador();
        endereco.setCep(cepLimpo);
        endereco.setUf(enderecoViaCep.uf());
        endereco.setEstado(enderecoViaCep.estado());
        endereco.setBairro(enderecoViaCep.bairro());
        endereco.setLogradouro(enderecoViaCep.logradouro());
        endereco.setTipoEndereco(reqDTO.endereco().tipo_endereco());
        endereco.setRegiao(enderecoViaCep.regiao());
        endereco.setColaborador(colaborador);

        colaborador.setEndereco(endereco);

        colaboradorRepository.save(colaborador);
    }

    @Transactional
    public ColaboradorRespostaDTO buscarPorId(Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador com ID " + id + " não encontrado."));

        EnderecoColaborador endereco = colaborador.getEndereco();
        EnderecoRespostaDTO enderecoDTO = null;

        if (endereco != null) {
            enderecoDTO = new EnderecoRespostaDTO(
                    endereco.getLogradouro(),
                    endereco.getBairro(),
                    endereco.getUf(),
                    endereco.getEstado(),
                    endereco.getCep(),
                    endereco.getRegiao()
            );
        }

        return new ColaboradorRespostaDTO(
                colaborador.getId(),
                colaborador.getNome(),
                colaborador.getEmail(),
                colaborador.getCpf(),
                colaborador.getCargo(),
                colaborador.getTelefone(),
                colaborador.getData_admissao(),
                colaborador.getData_demissao(),
                enderecoDTO
        );
    }



    @Transactional
    public Page<ColaboradorRespostaDTO> buscarTodosColaboradoresPaginados() {
        int paginaDesejada = 0;
        int tamanhoPagina = 10;

        Pageable pageable = PageRequest.of(paginaDesejada, tamanhoPagina, Sort.by("nome").ascending());
        Page<Colaborador> paginaColaboradores = colaboradorRepository.findByStatusAtivoTrue(pageable);

        return paginaColaboradores.map(colaborador -> {
            EnderecoColaborador endereco = colaborador.getEndereco();
            EnderecoRespostaDTO enderecoDTO = null;

            if (endereco != null) {
                enderecoDTO = new EnderecoRespostaDTO(
                        endereco.getLogradouro(), endereco.getBairro(),
                        endereco.getUf(), endereco.getEstado(),
                        endereco.getCep(), endereco.getRegiao()
                );
            }

            return new ColaboradorRespostaDTO(
                    colaborador.getId(), colaborador.getNome(), colaborador.getEmail(), colaborador.getCpf(),
                    colaborador.getCargo(), colaborador.getTelefone(), colaborador.getData_admissao(),
                    colaborador.getData_demissao(), enderecoDTO
            );
        });
    }

    @Transactional
    public void deletarPorId(Long id) {
        Colaborador colaborador = entityManager.find(Colaborador.class, id);

        if (colaborador == null) {
            throw new EntityNotFoundException("Colaborador com ID " + id + " não encontrado.");
        }

        entityManager.remove(colaborador);
    }

    @Transactional
    public void atualizarPorId(Long id, ColaboradorRequisicaoDTO reqDTO) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador com ID " + id + " não encontrado."));

        String cepLimpo = validarECepLimpo(reqDTO.endereco().cep());
        EnderecoRequisicaoDTO enderecoViaCep = viaCepClient.getEndereco(cepLimpo);
        validarEnderecoViaCep(enderecoViaCep);

        colaborador.setNome(reqDTO.nome());
        colaborador.setEmail(reqDTO.email());
        colaborador.setCpf(reqDTO.cpf());
        colaborador.setCargo(reqDTO.cargo());
        colaborador.setTelefone(reqDTO.telefone());
        colaborador.setData_admissao(reqDTO.data_admissao());
        colaborador.setData_demissao(reqDTO.data_demissao());

        EnderecoColaborador endereco = colaborador.getEndereco(); // já existente

        if (endereco == null) {
            endereco = new EnderecoColaborador();
            endereco.setColaborador(colaborador);
        }

        endereco.setCep(cepLimpo);
        endereco.setUf(enderecoViaCep.uf());
        endereco.setEstado(enderecoViaCep.estado());
        endereco.setBairro(enderecoViaCep.bairro());
        endereco.setLogradouro(enderecoViaCep.logradouro());
        endereco.setTipoEndereco(reqDTO.endereco().tipo_endereco());
        endereco.setRegiao(enderecoViaCep.regiao());

        colaborador.setEndereco(endereco);
        colaboradorRepository.save(colaborador);
    }


    @Transactional
    public void atualizarParcial(Long id, ColaboradorAtualizacaoParcialDTO dto) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador com ID " + id + " não encontrado."));

        if (dto.nome() != null && !dto.nome().isBlank()) colaborador.setNome(dto.nome());
        if (dto.email() != null && !dto.email().isBlank()) colaborador.setEmail(dto.email());
        if (dto.cpf() != null && !dto.cpf().isBlank()) colaborador.setCpf(dto.cpf());
        if (dto.cargo() != null && !dto.cargo().isBlank()) colaborador.setCargo(dto.cargo());
        if (dto.telefone() != null && !dto.telefone().isBlank()) colaborador.setTelefone(dto.telefone());
        if (dto.data_admissao() != null) colaborador.setData_admissao(dto.data_admissao());
        if (dto.data_demissao() != null) colaborador.setData_demissao(dto.data_demissao());

        if (dto.endereco() != null && dto.endereco().cep() != null) {
            String cepLimpo = validarECepLimpo(dto.endereco().cep());
            EnderecoRequisicaoDTO enderecoViaCep = viaCepClient.getEndereco(cepLimpo);
            validarEnderecoViaCep(enderecoViaCep);

            EnderecoColaborador endereco = colaborador.getEndereco();
            if (endereco == null) {
                endereco = new EnderecoColaborador();
                endereco.setColaborador(colaborador);
            }

            endereco.setCep(cepLimpo);
            endereco.setUf(enderecoViaCep.uf());
            endereco.setEstado(enderecoViaCep.estado());
            endereco.setBairro(enderecoViaCep.bairro());
            endereco.setLogradouro(enderecoViaCep.logradouro());
            endereco.setTipoEndereco(dto.endereco().tipo_endereco());
            endereco.setRegiao(enderecoViaCep.regiao());

            colaborador.setEndereco(endereco);
        }

        colaboradorRepository.save(colaborador);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ColaboradorRespostaDTO> buscarColaboradoresInativos() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());

        Page<Colaborador> pagina = colaboradorRepository.findByStatusAtivoFalse(pageable);

        return pagina.map(colaborador -> {
            EnderecoColaborador endereco = colaborador.getEndereco();
            EnderecoRespostaDTO enderecoDTO = null;

            if (endereco != null) {
                enderecoDTO = new EnderecoRespostaDTO(
                        endereco.getLogradouro(), endereco.getBairro(),
                        endereco.getUf(), endereco.getEstado(),
                        endereco.getCep(), endereco.getRegiao()
                );
            }

            return new ColaboradorRespostaDTO(
                    colaborador.getId(), colaborador.getNome(), colaborador.getEmail(), colaborador.getCpf(),
                    colaborador.getCargo(), colaborador.getTelefone(), colaborador.getData_admissao(),
                    colaborador.getData_demissao(), enderecoDTO
            );
        });
    }

    @Transactional
    public void desativarColaborador(Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador com ID " + id + " não encontrado."));

        colaborador.setStatusAtivo(false); //Desativa o colaborador
        colaboradorRepository.save(colaborador);
    }

    @Transactional
    public void ativarColaborador(Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador com ID " + id + " não encontrado."));

        colaborador.setStatusAtivo(true); // ativar o colaborador
        colaboradorRepository.save(colaborador);
    }



}
