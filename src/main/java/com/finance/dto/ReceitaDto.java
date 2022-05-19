package com.finance.dto;

import com.finance.domain.Receita;

public class ReceitaDto {

    private Double valor;
    private String descricao;
    private String data;

    public ReceitaDto(Receita receita) {
        this.valor = receita.getValor();
        this.descricao = receita.getDescricao();
        this.data = receita.getData().toString();
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
