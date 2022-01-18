package com.finance.form;

import com.finance.domain.Usuario;

import javax.validation.constraints.*;

public class UsuarioForm {

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 40)
    private String nome;

    @Email
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 6)
    private String senha;

    public Usuario converter() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        return usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
