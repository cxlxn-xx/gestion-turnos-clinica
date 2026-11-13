package ar.com.turnos.dao;

import ar.com.turnos
.entities.TurnosGenerales;
import ar.com.turnos
.entities.TurnosProfesionales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITurnosProfesionalesDao extends JpaRepository<TurnosProfesionales, String> {

    List<TurnosProfesionales> findByProfesionalIdAndTurnogeneralDiaId(Integer profesionalId, Integer diaId);

    boolean existsByProfesionalIdAndTurnogeneralId(Integer profesionalId, String turnogeneralId);

    /** Turnos generales activos que el profesional atiende un día, ordenados por horario */
    @Query("select tp.turnogeneral from TurnosProfesionales tp "
            + "where tp.profesional.id = ?1 and tp.turnogeneral.dia.id = ?2 and tp.turnogeneral.estado = true "
            + "order by tp.turnogeneral.horario.descrip")
    List<TurnosGenerales> findHorarios(Integer profesionalId, Integer diaId);
}
