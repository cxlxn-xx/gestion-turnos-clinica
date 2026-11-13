package ar.com.turnos.services;

import ar.com.turnos.entities.Profesionales;
import ar.com.turnos.entities.TurnosGenerales;

import java.util.List;
import java.util.Set;

public interface ITurnosProfesionalesService {


    Set<String> idsTurnosGenerales(Integer profesionalId, Integer diaId);

    /** Turnos generales activos que atiende el profesional un día (ordenados por horario) */
    List<TurnosGenerales> horarios(Integer profesionalId, Integer diaId);

    boolean atiende(Integer profesionalId, String turnogeneralId);

    /**
     * Deja asignados al profesional, para ese día, exactamente los turnos generales seleccionados.
     * Devuelve un mensaje con el resultado.
     */
    String sincronizar(Profesionales profesional, Integer diaId, List<String> seleccionados);
}
