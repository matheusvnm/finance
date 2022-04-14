package com.finance.form;

import com.finance.domain.Despesa;
import com.finance.domain.Usuario;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DespesaForm {

    @NotNull(message = "O campo valor é obrigatório")
    private Double valor;
    @NotEmpty(message = "O campo descrição é obrigatório")
    private String descricao;
    @NotEmpty(message = "O campo data é obrigatório")
    private String data;

    public Despesa converter(Usuario usuario) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Despesa despesa = new Despesa();
        despesa.setValor(this.valor);
        despesa.setDescricao(this.descricao);
        despesa.setData(LocalDate.parse(this.data, formatter));
        despesa.setUsuario(usuario);
        return despesa;
    }

    public Despesa atualizarDespesa(Despesa despesa) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        despesa.setValor(this.valor);
        despesa.setDescricao(this.descricao);
        despesa.setData(LocalDate.parse(this.data, formatter));
        return despesa;
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
