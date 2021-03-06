package com.finance.repository;

import com.finance.domain.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Boolean existsReceitaByDataBetweenAndDescricaoEquals(LocalDate startDate, LocalDate endDate,
                                                         String descricao);

    Optional<Receita> findFirstByIdAndUsuarioId(Long id, Long usuarioId);

    Boolean existsReceitaByDataBetweenAndDescricaoEqualsAndIdIsNot(LocalDate startDate, LocalDate endDate,
                                                                   String descricao, Long userId);

    Page<Receita> findAllByUsuarioId(Pageable pageable, Long usuarioId);

    Page<Receita> findAllByUsuarioIdAndDescricaoContaining(Pageable pageable, Long usuarioId,
                                                           String descricao);

    @Query("SELECT r FROM Receita r " +
            "WHERE r.usuario.id = :usuario_id AND MONTH(r.data) = :mes AND YEAR(r.data) = :ano")
    Page<Receita> findAllByUsuarioIdAndData_MesAndData_Ano(@Param("usuario_id") Long usuarioId,
                                                           @Param("mes") Integer mes,
                                                           @Param("ano") Integer ano, Pageable pageable);
    @Query("SELECT r FROM Receita r " +
            "WHERE r.usuario.id = :usuario_id AND MONTH(r.data) = :mes AND YEAR(r.data) = :ano")
    List<Receita> findAllByUsuarioIdAndData_MesAndData_Ano(@Param("usuario_id") Long usuarioId,
                                                           @Param("mes") Integer mes,
                                                           @Param("ano") Integer ano);

}