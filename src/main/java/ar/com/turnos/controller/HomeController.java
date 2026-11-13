package ar.com.turnos.controller;

import ar.com.turnos
.services.ITurnosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class HomeController {

    @Autowired
    ITurnosService turnosService;

    @RequestMapping("/")
    public String welcome(Model model, Authentication auth) {
        // El profesional (sin perfil de administrador) va directo a su agenda
        if (auth != null) {
            boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
            boolean profesional = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESIONAL"));
            if (profesional && !admin) {
                return "redirect:/agenda";
            }
        }
        model.addAttribute("message", "Gestión de Turnos - Clínica");
        model.addAttribute("turnosHoy", turnosService.contarActivos(LocalDate.now()));
        return "home";
    }
}