package ar.com.turnos.controller;

import ar.com.turnos
.entities.Dias;
import ar.com.turnos
.entities.Profesionales;
import ar.com.turnos
.services.IDiasService;
import ar.com.turnos
.services.IProfesionalesService;
import ar.com.turnos
.services.ITurnosGeneralesService;
import ar.com.turnos
.services.ITurnosProfesionalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Horarios en los que trabaja cada profesional: se eligen, por día, los turnos generales que atiende.
 */
@Controller
public class ProfesionalHorariosController {

    @Autowired
    IProfesionalesService profesionalesService;
    @Autowired
    IDiasService diasService;
    @Autowired
    ITurnosGeneralesService turnosGeneralesService;
    @Autowired
    ITurnosProfesionalesService turnosProfesionalesService;

    @RequestMapping(value = "/profesionales/{id}/horarios", method = RequestMethod.GET)
    public String index(@PathVariable Integer id, @RequestParam(required = false) Integer dia, Model model) {
        Profesionales profesional = profesionalesService.get(id);
        if (profesional == null) {
            return "redirect:/profesionales";
        }

        List<Dias> dias = diasService.getAll();
        if (dias.isEmpty()) {
            model.addAttribute("message", "No hay días cargados.");
            model.addAttribute("profesional", profesional);
            model.addAttribute("dias", dias);
            return "../profesionales/horarios";
        }

        // Día seleccionado (por defecto, el primero)
        Dias diaSel = dias.get(0);
        for (Dias d : dias) {
            if (dia != null && d.getId().equals(dia)) {
                diaSel = d;
            }
        }

        model.addAttribute("profesional", profesional);
        model.addAttribute("dias", dias);
        model.addAttribute("diaSel", diaSel);
        model.addAttribute("turnosGenerales", turnosGeneralesService.findByDia(diaSel.getId()));
        model.addAttribute("asignados", turnosProfesionalesService.idsTurnosGenerales(id, diaSel.getId()));
        return "../profesionales/horarios";
    }

    @RequestMapping(value = "/profesionales/{id}/horarios", method = RequestMethod.POST)
    public String guardar(@PathVariable Integer id, @RequestParam Integer dia,
                          @RequestParam(required = false) List<String> tg, RedirectAttributes redirect) {
        Profesionales profesional = profesionalesService.get(id);
        if (profesional == null) {
            return "redirect:/profesionales";
        }
        redirect.addFlashAttribute("message", turnosProfesionalesService.sincronizar(profesional, dia, tg));
        return "redirect:/profesionales/" + id + "/horarios?dia=" + dia;
    }
}
