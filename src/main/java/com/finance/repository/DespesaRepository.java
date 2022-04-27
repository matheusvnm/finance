package com.finance.repository;

import com.finance.domain.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Boolean existsDespesaByDataBetweenAndDescricaoEquals(LocalDate startDate, LocalDate endDate,
                                                         String descricao);

    Page<Despesa> findAllByUsuarioId(Pageable pageable, Long usuarioId);

    Page<Despesa> findAllByUsuarioIdAndDescricaoContaining(Pageable pageable, Long usuarioId,
                                                           String descricao);

    @Query("SELECT d FROM Despesa d " +
            "WHERE d.usuario.id = :usuario_id AND MONTH(d.data) = :mes AND YEAR(d.data) = :ano")
    Page<Despesa> findAllByUsuarioIdAndData_MesAndData_Ano(@Param("usuario_id") Long usuarioId,
                                                           @Param("mes") Integer mes,
                                                           @Param("ano") Integer ano,
                                                           Pageable pageable);

    @Query("SELECT d FROM Despesa d " +
            "WHERE d.usuario.id = :usuario_id AND MONTH(d.data) = :mes AND YEAR(d.data) = :ano")
    List<Despesa> findAllByUsuarioIdAndData_MesAndData_Ano(@Param("usuario_id") Long usuarioId,
                                                           @Param("mes") Integer mes,
                                                           @Param("ano") Integer ano);
}