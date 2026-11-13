package ar.com.turnos.controller;

import ar.com.turnos.entities.Turnos;
import ar.com.turnos.entities.TurnosGenerales;
import ar.com.turnos.services.IObrasSocialesService;
import ar.com.turnos.services.IPacientesService;
import ar.com.turnos.services.IProfesionalesService;
import ar.com.turnos.services.ITurnosService;
import ar.com.turnos.utiles.EstadosTurno;
import ar.com.turnos.utiles.PageWrapper;
import ar.com.turnos
.utiles.Utiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Asignación de turnos: se elige fecha, profesional, paciente, obra social y horario.
 * Los turnos no se borran: se anulan. Un turno admite returnos (copias en otras fechas).
 */
@Controller
public class TurnosController {

    @Autowired
    ITurnosService entityService;
    @Autowired
    IProfesionalesService profesionalesService;
    @Autowired
    IPacientesService pacientesService;
    @Autowired
    IObrasSocialesService obrasSocialesService;

    // ------------------------------------------------------------ listado
    @RequestMapping(value = "/turnos", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                       @RequestParam(required = false) Integer profesional,
                       @RequestParam(required = false) String q) {

        Page<Turnos> pagina = entityService.buscar(fecha, profesional, q, pageable);

        // La URL del paginado conserva los filtros
        UriComponentsBuilder url = UriComponentsBuilder.fromPath("/turnos");
        if (fecha != null) url.queryParam("fecha", fecha.toString());
        if (profesional != null) url.queryParam("profesional", profesional);
        if (!Utiles.vacio(q)) url.queryParam("q", q.trim());

        PageWrapper<Turnos> page = new PageWrapper<Turnos>(pagina, url.build().encode().toUriString());
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("fecha", fecha);
        model.addAttribute("profesionalSel", profesional);
        model.addAttribute("q", q);
        model.addAttribute("profesionales", profesionalesService.getAll());
        return "../turnos/index";
    }

    @RequestMapping("/turnos/refresh")
    public String refresh() {
        return "redirect:/turnos";
    }

    // ------------------------------------------------------------ alta / edición
    @RequestMapping("/turnos/create")
    public String create(Model model) {
        Turnos entity = new Turnos();
        entity.setFecha(LocalDate.now());
        cargarFormulario(model, entity);
        return "../turnos/edit";
    }

    @RequestMapping("/turnos/edit/{id}")
    public String edit(@PathVariable String id, Model model, RedirectAttributes redirect) {
        Turnos entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }
        if (!EstadosTurno.ACTIVO.equals(entity.getEstado())) {
            redirect.addFlashAttribute("message", "El turno está anulado y no se puede modificar.");
            return refresh();
        }
        cargarFormulario(model, entity);
        return "../turnos/edit";
    }

    // Nuevo turno a partir de otro (returno): mismos datos, otra fecha
    @RequestMapping("/turnos/returno/{id}")
    public String returno(@PathVariable String id, Model model, RedirectAttributes redirect) {
        Turnos original = entityService.get(id);
        if (original == null) {
            return refresh();
        }
        if (!EstadosTurno.ACTIVO.equals(original.getEstado())) {
            redirect.addFlashAttribute("message", "No se puede generar un returno de un turno anulado.");
            return refresh();
        }
        Turnos entity = new Turnos();
        entity.setPaciente(original.getPaciente());
        entity.setProfesional(original.getProfesional());
        entity.setObrasocial(original.getObrasocial());
        entity.setDescrip(original.getDescrip());
        // Todos los returnos apuntan al turno original
        entity.setReturno(original.getReturno() != null ? original.getReturno() : original);
        cargarFormulario(model, entity);
        return "../turnos/edit";
    }

    // Recarga el formulario con los horarios del profesional para la fecha elegida
    @RequestMapping(value = "/turnos/horarios", method = RequestMethod.POST)
    public String horarios(Model model, @ModelAttribute("entity") Turnos entity, BindingResult result) {
        cargarFormulario(model, entity);
        return "../turnos/edit";
    }

    @RequestMapping(value = "/turnos", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Turnos entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";

        if (!Utiles.vacio(entity.getId())) {
            Turnos actual = entityService.get(entity.getId());
            if (actual == null || !EstadosTurno.ACTIVO.equals(actual.getEstado())) {
                errores += "El turno no existe o está anulado. ";
            }
        }
        if (errores.isEmpty()) {
            errores = entityService.validar(entity);
        }

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores);
            cargarFormulario(model, entity);
            return "../turnos/edit";
        }

        entityService.save(entity);
        return "redirect:/turnos";
    }

    // ------------------------------------------------------------ anulación
    @RequestMapping("/turnos/anular/{id}")
    public String anular(@PathVariable String id, RedirectAttributes redirect) {
        Turnos entity = entityService.get(id);
        if (entity != null && EstadosTurno.ACTIVO.equals(entity.getEstado())) {
            entityService.anular(entity);
            redirect.addFlashAttribute("message", "Turno anulado.");
        }
        return refresh();
    }

    // ------------------------------------------------------------ auxiliares
    private void cargarFormulario(Model model, Turnos entity) {
        List<TurnosGenerales> horarios = entityService.horariosDisponibles(entity.getProfesional(), entity.getFecha(), entity.getId());

        // Si el horario elegido ya no corresponde (cambió la fecha o el profesional), se descarta
        if (entity.getTurnogeneral() != null) {
            boolean sigue = false;
            for (TurnosGenerales h : horarios) {
                if (h.getId().equals(entity.getTurnogeneral().getId())) sigue = true;
            }
            if (!sigue) entity.setTurnogeneral(null);
        }

        model.addAttribute("entity", entity);
        model.addAttribute("horarios", horarios);
        model.addAttribute("profesionales", profesionalesService.getAll());
        model.addAttribute("pacientes", pacientesService.getAll());
        model.addAttribute("obrassociales", obrasSocialesService.getAll());
    }
}