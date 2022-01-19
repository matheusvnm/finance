package com.finance.form;

import com.finance.domain.Receita;
import com.finance.domain.Usuario;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ReceitaForm {

    private Double valor;
    private String descricao;
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
