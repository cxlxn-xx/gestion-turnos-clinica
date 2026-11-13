package ar.com.turnos.services;

import ar.com.turnos
.entities.Profesionales;
import ar.com.turnos
.entities.Turnos;
import ar.com.turnos
.entities.TurnosGenerales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ITurnosService {

    Page<Turnos> buscar(LocalDate fecha, Integer profesionalId, String texto, Pageable pageable);

    Turnos get(String id);

    /**
     * Horarios (turnos generales) que el profesional atiende el día de la fecha y que se pueden asignar.
     * turnoActualId: turno que se está editando (para no contarlo como ocupado), puede ser null.
     */
    List<TurnosGenerales> horariosDisponibles(Profesionales profesional, LocalDate fecha, String turnoActualId);

    /** Valida las reglas del turno. Devuelve el texto de los errores, o vacío si está todo bien. */
    String validar(Turnos turno);

    /** Crea el turno (estado ACTIVO) o, si ya existe, actualiza sus datos editables. */
    void save(Turnos turno);

    void anular(Turnos turno);

    List<Turnos> agenda(Integer profesionalId, LocalDate desde, LocalDate hasta);

    long contarActivos(LocalDate fecha);
}
