package com.finance.domain;


import com.finance.enums.CategoriaEnum;

import java.util.ArrayList;
import java.util.List;

public class Resumo {
    private double valorDespesas = 0.0;
    private double valorReceitas = 0.0;
    private double saldoFinal = 0.0;
    private List<DespesaPorCategoria> despesaPorCategorias = new ArrayList<>();


    public void calcularDespesas(List<Despesa> despesas) {
        valorDespesas = despesas.parallelStream()
                .mapToDouble(Despesa::getValor)
                .sum();
    }

    public void calcularReceitas(List<Receita> receitas) {
        valorReceitas = receitas.parallelStream()
                .mapToDouble(Receita::getValor)
                .sum();
    }

    public void calcularSaldoFinal() {
        saldoFinal = valorReceitas - valorDespesas;
    }

    public void calcularDespesasPorCategoria(List<Despesa> despesas) {
        for (CategoriaEnum categoriaEnum : CategoriaEnum.values())
            despesaPorCategorias.add(new DespesaPorCategoria(categoriaEnum.toString(), 0.0));

        for (Despesa despesa : despesas)
            for (DespesaPorCategoria despesaCategoria : despesaPorCategorias)
                if (despesaCategoria.getDespesaNome()
                        .equals(despesa.getCategoria()
                                .toString())) despesaCategoria.addUmaDespesa(despesa.getValor());
    }

    public double getValorDespesas() {
        return valorDespesas;
    }

    public void setValorDespesas(double valorDespesas) {
        this.valorDespesas = valorDespesas;
    }

    public double getValorReceitas() {
        return valorReceitas;
    }

    public void setValorReceitas(double valorReceitas) {
        this.valorReceitas = valorReceitas;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public List<DespesaPorCategoria> getDespesaPorCategorias() {
        return despesaPorCategorias;
    }

    public void setDespesaPorCategorias(
            List<DespesaPorCategoria> despesaPorCategorias) {
        this.despesaPorCategorias = despesaPorCategorias;
    }
}
