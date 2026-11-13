package ar.com.turnos.controller;

import ar.com.turnos.entities.Profesionales;
import ar.com.turnos.entities.Usuarios;
import ar.com.turnos.services.IProfesionalesService;
import ar.com.turnos.services.ITurnosService;
import ar.com.turnos.services.IUsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;

/** Perfil Profesional: consulta de su propia agenda de turnos. */
@Controller
public class AgendaController {

    @Autowired
    ITurnosService turnosService;
    @Autowired
    IProfesionalesService profesionalesService;
    @Autowired
    IUsuariosService usuariosService;

    @RequestMapping(value = "/agenda", method = RequestMethod.GET)
    public String agenda(Model model, Principal principal,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        if (desde == null) desde = LocalDate.now();
        if (hasta == null || hasta.isBefore(desde)) hasta = desde.plusDays(30);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        // El usuario logueado se relaciona con su profesional
        Usuarios usuario = usuariosService.findByUsuario(principal.getName());
        Profesionales profesional = usuario == null ? null : profesionalesService.findByUsuario(usuario.getId());
        if (profesional == null) {
            model.addAttribute("message", "Su usuario no está asociado a ningún profesional. Consulte con el administrador.");
            return "../agenda/index";
        }

        model.addAttribute("profesional", profesional);
        model.addAttribute("turnos", turnosService.agenda(profesional.getId(), desde, hasta));
        return "../agenda/index";
    }
}
