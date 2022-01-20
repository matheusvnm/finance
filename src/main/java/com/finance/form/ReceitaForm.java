package com.finance.form;

import com.finance.domain.Receita;
import com.finance.domain.Usuario;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReceitaForm {

    @NotEmpty
    @NotNull
    @NotBlank(message = "O campo valor é obrigatório")
    private Double valor;
    @NotBlank(message = "O campo descrição é obrigatório")
    private String descricao;
    @NotBlank(message = "O campo data é obrigatório")
    private String data;

    public Receita converter(Usuario usuario) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Receita receita = new Receita();
        receita.setValor(this.valor);
        receita.setDescricao(this.descricao);
        receita.setData(LocalDate.parse(this.data, formatter));
        receita.setUsuario(usuario);
        return receita;
    }

    public Receita atualizarReceita(Receita receita) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        receita.setValor(this.valor);
        receita.setDescricao(this.descricao);
        receita.setData(LocalDate.parse(this.data, formatter));
        return receita;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
