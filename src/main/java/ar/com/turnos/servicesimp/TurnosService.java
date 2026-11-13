package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.ITurnosDao;
import ar.com.turnos
.entities.Profesionales;
import ar.com.turnos
.entities.Turnos;
import ar.com.turnos
.entities.TurnosGenerales;
import ar.com.turnos
.services.ITurnosProfesionalesService;
import ar.com.turnos
.services.ITurnosService;
import ar.com.turnos
.utiles.EstadosTurno;
import ar.com.turnos
.utiles.Utiles;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TurnosService implements ITurnosService {

    @Autowired
    private ITurnosDao entityDao;
    @Autowired
    private ITurnosProfesionalesService turnosProfesionalesService;

    // Política de la institución: ¿un profesional puede atender varios pacientes en el mismo horario?
    @Value("${turnos.permitir-simultaneos:false}")
    private boolean permitirSimultaneos;

    public Page<Turnos> buscar(LocalDate fecha, Integer profesionalId, String texto, Pageable pageable) {
        String q = Utiles.vacio(texto) ? null : "%" + texto.trim().toLowerCase() + "%";
        return entityDao.buscar(fecha, profesionalId, q, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    public Turnos get(String id) {
        return entityDao.findById(id).orElse(null);
    }

    public List<TurnosGenerales> horariosDisponibles(Profesionales profesional, LocalDate fecha, String turnoActualId) {
        if (profesional == null || fecha == null) {
            return new ArrayList<TurnosGenerales>();
        }
        // El id del día coincide con DayOfWeek: 1 = Lunes ... 7 = Domingo
        List<TurnosGenerales> horarios = turnosProfesionalesService.horarios(profesional.getId(), fecha.getDayOfWeek().getValue());
        if (permitirSimultaneos) {
            return horarios;
        }

        Set<String> ocupados = new HashSet<String>();
        for (Turnos t : entityDao.findByProfesionalIdAndFechaAndEstado(profesional.getId(), fecha, EstadosTurno.ACTIVO)) {
            if (turnoActualId == null || !t.getId().equals(turnoActualId)) {
                ocupados.add(t.getTurnogeneral().getId());
            }
        }
        List<TurnosGenerales> libres = new ArrayList<TurnosGenerales>();
        for (TurnosGenerales tg : horarios) {
            if (!ocupados.contains(tg.getId())) {
                libres.add(tg);
            }
        }
        return libres;
    }

    public String validar(Turnos t) {
        String errores = "";
        if (t.getFecha() == null) errores += "Fecha incorrecta. ";
        if (t.getProfesional() == null) errores += "Debe seleccionar un profesional. ";
        if (t.getPaciente() == null) errores += "Debe seleccionar un paciente. ";
        if (t.getObrasocial() == null) errores += "Debe seleccionar una obra social. ";
        if (t.getTurnogeneral() == null) errores += "Debe seleccionar un horario. ";

        boolean nuevo = Utiles.vacio(t.getId());
        if (nuevo && t.getFecha() != null && t.getFecha().isBefore(LocalDate.now())) {
            errores += "La fecha no puede ser anterior a hoy. ";
        }
        if (t.getObrasocial() != null && t.getObrasocial().isSuspendida()) {
            errores += "La obra social está suspendida. ";
        }

        if (t.getFecha() != null && t.getProfesional() != null && t.getTurnogeneral() != null) {
            TurnosGenerales tg = t.getTurnogeneral();

            if (!tg.isEstado()) {
                errores += "El horario seleccionado está inactivo. ";
            }
            if (tg.getDia().getId() != t.getFecha().getDayOfWeek().getValue()) {
                errores += "La fecha no corresponde al día del horario (" + tg.getDia().getDescrip() + "). ";
            }
            if (!turnosProfesionalesService.atiende(t.getProfesional().getId(), tg.getId())) {
                errores += "El profesional no atiende en ese día y horario. ";
            }

            // Política: un solo turno activo por profesional, fecha y horario (salvo que se permitan simultáneos)
            if (!permitirSimultaneos) {
                for (Turnos o : entityDao.findByProfesionalIdAndFechaAndEstado(t.getProfesional().getId(), t.getFecha(), EstadosTurno.ACTIVO)) {
                    if (o.getTurnogeneral().getId().equals(tg.getId()) && (nuevo || !o.getId().equals(t.getId()))) {
                        errores += "El profesional ya tiene un turno asignado en ese horario. ";
                        break;
                    }
                }
            }
            // Un paciente no puede tener dos turnos activos en la misma fecha y horario
            if (t.getPaciente() != null) {
                for (Turnos o : entityDao.findByPacienteIdAndFechaAndEstado(t.getPaciente().getId(), t.getFecha(), EstadosTurno.ACTIVO)) {
                    if (o.getTurnogeneral().getId().equals(tg.getId()) && (nuevo || !o.getId().equals(t.getId()))) {
                        errores += "El paciente ya tiene un turno en esa fecha y horario. ";
                        break;
                    }
                }
            }
        }
        return errores.trim();
    }

    @Transactional
    public void save(Turnos t) {
        if (Utiles.vacio(t.getId())) {
            t.setId(Utiles.getUUID());
            t.setEstado(EstadosTurno.ACTIVO);
            t.setFechahora(LocalDateTime.now());
            entityDao.save(t);
        } else {
            Turnos actual = get(t.getId());
            actual.setFecha(t.getFecha());
            actual.setProfesional(t.getProfesional());
            actual.setPaciente(t.getPaciente());
            actual.setObrasocial(t.getObrasocial());
            actual.setTurnogeneral(t.getTurnogeneral());
            actual.setDescrip(t.getDescrip());
            entityDao.save(actual);
        }
    }

    @Transactional
    public void anular(Turnos t) {
        t.setEstado(EstadosTurno.ANULADO);
        entityDao.save(t);
    }

    public List<Turnos> agenda(Integer profesionalId, LocalDate desde, LocalDate hasta) {
        return entityDao.agenda(profesionalId, desde, hasta);
    }

    public long contarActivos(LocalDate fecha) {
        return entityDao.countByFechaAndEstado(fecha, EstadosTurno.ACTIVO);
    }
}
