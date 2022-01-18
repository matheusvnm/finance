package com.finance.dto;

import com.finance.domain.Usuario;

public class UsuarioDto {
    private String nome;
    private String email;

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
