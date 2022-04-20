package com.finance.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finance.enums.CategoriaEnum;
import com.finance.repository.DespesaRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double valor;
    private String descricao;
    private LocalDate data;
    private CategoriaEnum categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEnum categoriaEnum) {
        this.categoria = categoriaEnum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Despesa other = (Despesa) obj;
        return other.descricao.equalsIgnoreCase(this.descricao);
    }

    private LocalDate getDataInicialDoMes() {
        String dia = "01";
        String ano = String.valueOf(this.getData()
                .getYear());
        String mes = String.valueOf(this.getData()
                .getMonth()
                .getValue());
        mes = Integer.parseInt(mes) < 10 ? "0" + mes : mes;
        return LocalDate.parse(dia + "/" + mes
                + "/" + ano, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private LocalDate getDataFinalDoMes() {
        return this.getDataInicialDoMes()
                .plusMonths(1)
                .minusDays(1);
    }

    public boolean existeDespesaComMesmaDescricaoNoMes(DespesaRepository despesaRepository) {
        return despesaRepository.existsDespesaByDataBetweenAndDescricaoEquals(
                this.getDataInicialDoMes(),
                this.getDataFinalDoMes(), this.descricao);
    }
}