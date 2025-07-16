package br.com.raroacademy.projetofinal.service.usuario.CreateUser;

import br.com.raroacademy.projetofinal.interfaces.ICommand;
import br.com.raroacademy.projetofinal.validation.usuario.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserCommand implements ICommand {
    @NotBlank(message = "Nome não pode ficar em branco")
    private final String nome;

    @Email(message = "Formato de e-mail inválido")
    @NotBlank(message = "E-mail não pode ficar em branco")
    private final String email;

    @SenhaForte
    private final String senha;

    public CreateUserCommand(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}
