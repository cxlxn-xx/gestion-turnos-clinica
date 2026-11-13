package ar.com.turnos.dao;

import ar.com.turnos
.entities.TurnosGenerales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITurnosGeneralesDao extends JpaRepository<TurnosGenerales, String> {

    @Query("select c from TurnosGenerales c where c.dia.descrip like ?1 or c.horario.descrip like ?1")
    List<TurnosGenerales> findByDescrip(String name);

    Page<TurnosGenerales> findAll(Pageable pageable);

    TurnosGenerales findOneByDiaIdAndHorarioId(Integer diaId, Integer horarioId);

    List<TurnosGenerales> findByDiaIdOrderByHorarioDescripAsc(Integer diaId);

}
