package ar.com.turnos.dao;

import ar.com.turnos
.entities.Turnos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITurnosDao extends JpaRepository<Turnos, String> {

    /** Listado con filtros opcionales (fecha, profesional y texto del paciente). q ya viene en minúsculas con %. */
    @Query("select t from Turnos t "
            + "where (:fecha is null or t.fecha = :fecha) "
            + "and (:profesionalId is null or t.profesional.id = :profesionalId) "
            + "and (:q is null or lower(t.paciente.apellido) like :q or lower(t.paciente.nombre) like :q) "
            + "order by t.fecha desc, t.turnogeneral.horario.descrip asc")
    Page<Turnos> buscar(@Param("fecha") LocalDate fecha, @Param("profesionalId") Integer profesionalId,
                        @Param("q") String q, Pageable pageable);

    /** Agenda de un profesional entre dos fechas */
    @Query("select t from Turnos t where t.profesional.id = ?1 and t.fecha between ?2 and ?3 "
            + "order by t.fecha, t.turnogeneral.horario.descrip")
    List<Turnos> agenda(Integer profesionalId, LocalDate desde, LocalDate hasta);

    List<Turnos> findByProfesionalIdAndFechaAndEstado(Integer profesionalId, LocalDate fecha, String estado);

    List<Turnos> findByPacienteIdAndFechaAndEstado(Integer pacienteId, LocalDate fecha, String estado);

    boolean existsByProfesionalIdAndTurnogeneralIdAndEstadoAndFechaGreaterThanEqual(
            Integer profesionalId, String turnogeneralId, String estado, LocalDate fecha);

    long countByFechaAndEstado(LocalDate fecha, String estado);
}
