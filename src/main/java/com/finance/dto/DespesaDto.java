package com.finance.dto;

import com.finance.domain.Despesa;

public class DespesaDto {

    private Double valor;
    private String descricao;
    private String data;
    private String categoria;

    public DespesaDto(Despesa despesa) {
        this.valor = despesa.getValor();
        this.descricao = despesa.getDescricao();
        this.data = despesa.getData().toString();
        this.categoria = despesa.getCategoria().toString();
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


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
