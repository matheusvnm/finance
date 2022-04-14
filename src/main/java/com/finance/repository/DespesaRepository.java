package com.finance.repository;

import com.finance.domain.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Boolean existsDespesaByDataBetweenAndDescricaoEquals(LocalDate startDate, LocalDate endDate,
                                                         String descricao);
    Page<Despesa> findAllByUsuarioId(Pageable pageable, Long usuarioId);
}