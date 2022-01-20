package com.finance.repository;

import com.finance.domain.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Month;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Boolean existsReceitaByDataBetweenAndDescricaoEquals(LocalDate startDate, LocalDate endDate, String descricao);
    Page<Receita> findAllByUsuarioId(Pageable pageable, Long usuarioId);
}