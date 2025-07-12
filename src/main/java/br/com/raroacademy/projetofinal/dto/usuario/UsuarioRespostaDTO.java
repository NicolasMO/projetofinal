package br.com.raroacademy.projetofinal.dto.usuario;

import br.com.raroacademy.projetofinal.model.usuario.Usuario;

public record UsuarioRespostaDTO(Long id, String nome, String email) {

    public UsuarioRespostaDTO(Usuario usuario) {
        this((long) usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
