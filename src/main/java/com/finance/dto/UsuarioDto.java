package com.finance.dto;

import com.finance.domain.Usuario;

public class UsuarioDto {
    private final String nome;
    private final String email;

    public UsuarioDto(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
