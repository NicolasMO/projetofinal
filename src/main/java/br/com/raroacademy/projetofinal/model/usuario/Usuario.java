package br.com.raroacademy.projetofinal.model.usuario;

import br.com.raroacademy.projetofinal.dto.usuario.UsuarioRequisicaoDTO;
import br.com.raroacademy.projetofinal.service.usuario.CreateUser.CreateUserCommand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    @JsonIgnore
    private String senha;
    @JsonIgnore
    private boolean ativo = false;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenUsuario> tokens = new ArrayList<>();

    public Usuario getUserModel(UsuarioRequisicaoDTO dto) {
        nome = dto.nome();
        email = dto.email();
        senha = getBCrypt(dto.senha());
        return this;
    }

    public Usuario getUserModel(CreateUserCommand dto) {
        nome = dto.getNome();
        email = dto.getEmail();
        senha = getBCrypt(dto.getSenha());
        return this;
    }

    protected String getBCrypt(String senha) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(senha);
    }
}