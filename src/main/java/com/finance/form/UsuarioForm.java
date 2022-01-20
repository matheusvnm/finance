package com.finance.form;

import com.finance.config.security.JwtService;
import com.finance.domain.Usuario;

import javax.validation.constraints.*;

public class UsuarioForm {

    @NotNull
    @NotEmpty
    @NotBlank(message = "O campo nome é obrigatório")
    @Size(min = 1, max = 40)
    private String nome;

    @Email
    @NotBlank(message = "O campo e-mail é obrigatório")
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank(message = "O campo senha é obrigatório")
    @Size(min = 6)
    private String senha;

    public Usuario converter(JwtService jwtService) {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(criptografarSenha(this.senha, jwtService));
        return usuario;
    }

    private String criptografarSenha(String senha, JwtService jwtService) {
        return jwtService.criptografarSenha(senha);
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
