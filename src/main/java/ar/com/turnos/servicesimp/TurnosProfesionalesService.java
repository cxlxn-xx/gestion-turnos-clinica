package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.ITurnosDao;
import ar.com.turnos
.dao.ITurnosGeneralesDao;
import ar.com.turnos
.dao.ITurnosProfesionalesDao;
import ar.com.turnos
.entities.Profesionales;
import ar.com.turnos
.entities.TurnosGenerales;
import ar.com.turnos
.entities.TurnosProfesionales;
import ar.com.turnos
.services.ITurnosProfesionalesService;
import ar.com.turnos
.utiles.EstadosTurno;
import ar.com.turnos
.utiles.Utiles;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TurnosProfesionalesService implements ITurnosProfesionalesService {

    @Autowired
    private ITurnosProfesionalesDao entityDao;
    @Autowired
    private ITurnosGeneralesDao turnosGeneralesDao;
    @Autowired
    private ITurnosDao turnosDao;

    public Set<String> idsTurnosGenerales(Integer profesionalId, Integer diaId) {
        Set<String> ids = new HashSet<String>();
        for (TurnosProfesionales tp : entityDao.findByProfesionalIdAndTurnogeneralDiaId(profesionalId, diaId)) {
            ids.add(tp.getTurnogeneral().getId());
        }
        return ids;
    }

    public List<TurnosGenerales> horarios(Integer profesionalId, Integer diaId) {
        return entityDao.findHorarios(profesionalId, diaId);
    }

    public boolean atiende(Integer profesionalId, String turnogeneralId) {
        return entityDao.existsByProfesionalIdAndTurnogeneralId(profesionalId, turnogeneralId);
    }

    @Transactional
    public String sincronizar(Profesionales profesional, Integer diaId, List<String> seleccionados) {
        Set<String> marcados = seleccionados == null ? new HashSet<String>() : new HashSet<String>(seleccionados);

        Map<String, TurnosProfesionales> actuales = new HashMap<String, TurnosProfesionales>();
        for (TurnosProfesionales tp : entityDao.findByProfesionalIdAndTurnogeneralDiaId(profesional.getId(), diaId)) {
            actuales.put(tp.getTurnogeneral().getId(), tp);
        }

        int agregados = 0, quitados = 0, bloqueados = 0;
        LocalDate hoy = LocalDate.now();

        for (TurnosGenerales tg : turnosGeneralesDao.findByDiaIdOrderByHorarioDescripAsc(diaId)) {
            boolean marcado = marcados.contains(tg.getId());
            TurnosProfesionales existente = actuales.get(tg.getId());

            if (marcado && existente == null) {
                TurnosProfesionales tp = new TurnosProfesionales();
                tp.setId(Utiles.getUUID());
                tp.setProfesional(profesional);
                tp.setTurnogeneral(tg);
                entityDao.save(tp);
                agregados++;
            } else if (!marcado && existente != null) {
                // No se quita un horario si el profesional tiene turnos activos pendientes en él
                if (turnosDao.existsByProfesionalIdAndTurnogeneralIdAndEstadoAndFechaGreaterThanEqual(
                        profesional.getId(), tg.getId(), EstadosTurno.ACTIVO, hoy)) {
                    bloqueados++;
                } else {
                    entityDao.delete(existente);
                    quitados++;
                }
            }
        }

        String msg = "Horarios guardados (agregados: " + agregados + ", quitados: " + quitados + ").";
        if (bloqueados > 0) {
            msg += " " + bloqueados + " horario(s) no se quitaron porque tienen turnos activos pendientes.";
        }
        return msg;
    }
}
