package br.com.raroacademy.projetofinal.service.colaborador;

import br.com.raroacademy.projetofinal.client.ViaCepClient;
import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRequisicaoDTO;
import br.com.raroacademy.projetofinal.dto.colaborador.ColaboradorRespostaDTO;
import br.com.raroacademy.projetofinal.dto.colaborador.EnderecoRespostaDTO;
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
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public void salvarColaborador(ColaboradorRequisicaoDTO reqDTO) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(reqDTO.nome());
        colaborador.setEmail(reqDTO.email());
        colaborador.setCpf(reqDTO.cpf());
        colaborador.setCargo(reqDTO.cargo());
        colaborador.setTelefone(reqDTO.telefone());
        colaborador.setData_admissao(reqDTO.data_admissao());
        colaborador.setData_demissao(reqDTO.data_demissao());

        colaborador = colaboradorRepository.save(colaborador);

        String cep = reqDTO.endereco().cep();
        var enderecoViaCep = viaCepClient.getEndereco(cep);

        EnderecoColaborador endereco = new EnderecoColaborador();
        endereco.setColaborador(colaborador);
        endereco.setCep(cep);
        endereco.setUf(enderecoViaCep.uf());
        endereco.setEstado(enderecoViaCep.estado());
        endereco.setBairro(enderecoViaCep.bairro());
        endereco.setLogradouro(enderecoViaCep.logradouro());
        endereco.setTipoEndereco(reqDTO.endereco().tipo_endereco());
        endereco.setRegiao(enderecoViaCep.regiao());

        enderecoColaboradorRepository.save(endereco);
    }

    public  ColaboradorRespostaDTO buscarPorId(Long id){
        return entityManager.find(ColaboradorRespostaDTO.class, id);
    }
    @Transactional
    public List<Colaborador> buscarTodosColaboradores(){
        //   String jplq = """
        //           SELECT c FROM Colaborador c
        //           """;
        //   return entityManager.createQuery(jplq, ColaboradorRespostaDTO.class).getResultList();
        return colaboradorRepository.findAll();
    }

    @Transactional
    public void deletarPorId(Long id) {
        Colaborador colaborador = entityManager.find(Colaborador.class, id);

        if (colaborador == null) {
            throw new EntityNotFoundException("Colaborador com ID " + id + " não encontrado.");
        }

        entityManager.remove(colaborador);
    }


   //public void salvar(ColaboradorRequisicaoDTO colaboradorRequisicaoDTO){
   //    Colaborador colaborador = new Colaborador();
   //    colaborador.setNome(colaboradorRequisicaoDTO.nome());
   //    colaborador.setEmail(colaboradorRequisicaoDTO.email());
   //    colaborador.setCpf(colaboradorRequisicaoDTO.cpf());
   //    colaborador.setTelefone(colaboradorRequisicaoDTO.telefone());
   //    colaborador.setDataAdmissao(colaboradorRequisicaoDTO.data_admissao());
   //    Colaborador teste = colaboradorRepository.save(colaborador);
   //    System.out.println(colaboradorRequisicaoDTO.endereco());


   //  EnderecoColaborador endereco = new EnderecoColaborador();
   //  endereco.setColaborador(teste);
   //  endereco.setCep(colaboradorRequisicaoDTO.endereco().cep());
   //  endereco.setTipoEndereco(colaboradorRequisicaoDTO.endereco().tipo_endereco());
   //  endereco.setLogradouro(colaboradorRequisicaoDTO.endereco().logradouro());
   //  endereco.setBairro(colaboradorRequisicaoDTO.endereco().bairro());
   //  endereco.setUf(colaboradorRequisicaoDTO.endereco().uf());
   //  endereco.setEstado(colaboradorRequisicaoDTO.endereco().estado());
   //  endereco.setRegiao(colaboradorRequisicaoDTO.endereco().regiao());

   //  enderecoColaboradorRepository.save(endereco);
   //}

    //public Colaborador buscarPorId(Long id) {
    //    return colaboradorRepository.findById(id)
    //            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    //}
//
    //public List<Colaborador> listarTodos() {
    //    return null;
    //}


}
