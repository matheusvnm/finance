package com.finance.domain;


public class DespesaPorCategoria {
    private String despesaNome;
    private double total;

    public DespesaPorCategoria(String despesaNome, double total) {
        this.despesaNome = despesaNome;
        this.total = total;
    }

    public void addUmaDespesa(double valor) {
        total += valor;
    }

    public String getDespesaNome() {
        return despesaNome;
    }

    public void setDespesaNome(String despesaNome) {
        this.despesaNome = despesaNome;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
