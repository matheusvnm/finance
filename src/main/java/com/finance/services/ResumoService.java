package com.finance.services;

import com.finance.domain.Despesa;
import com.finance.domain.Receita;
import com.finance.domain.Resumo;
import com.finance.repository.DespesaRepository;
import com.finance.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumoService {
    @Autowired
    public DespesaRepository despesaRepository;

    @Autowired
    public ReceitaRepository receitaRepository;

    public Resumo obterResumo(Long usuarioId, Integer mes, Integer ano) {
        Resumo resumo = new Resumo();
        List<Receita> receitas = receitaRepository.findAllByUsuarioIdAndData_MesAndData_Ano(
                usuarioId, mes, ano);
        List<Despesa> despesas = despesaRepository.findAllByUsuarioIdAndData_MesAndData_Ano(
                usuarioId, mes, ano);
        resumo.calcularDespesas(despesas);
        resumo.calcularReceitas(receitas);
        resumo.calcularSaldoFinal();
        resumo.calcularDespesasPorCategoria(despesas);
        return resumo;
    }
}
