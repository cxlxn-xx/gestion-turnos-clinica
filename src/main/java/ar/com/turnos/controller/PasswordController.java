package ar.com.turnos.controller;

import ar.com.turnos
.entities.Usuarios;
import ar.com.turnos
.services.IUsuariosService;
import ar.com.turnos
.utiles.Utiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/** Cualquier usuario logueado puede cambiar su propia contraseña. */
@Controller
public class PasswordController {

    private static final int LARGO_MINIMO = 6;

    @Autowired
    IUsuariosService usuariosService;

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String form() {
        return "../password/edit";
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String cambiar(Model model, Principal principal,
                          @RequestParam(required = false, defaultValue = "") String actual,
                          @RequestParam(required = false, defaultValue = "") String nueva,
                          @RequestParam(required = false, defaultValue = "") String confirmar) {

        Usuarios usuario = usuariosService.findByUsuario(principal.getName());

        String errores = "";
        if (usuario == null || !usuariosService.passwordCoincide(usuario, actual)) {
            errores += "La contraseña actual es incorrecta. ";
        }
        if (Utiles.vacio(nueva) || nueva.length() < LARGO_MINIMO) {
            errores += "La nueva contraseña debe tener al menos " + LARGO_MINIMO + " caracteres. ";
        }
        if (!nueva.equals(confirmar)) {
            errores += "La confirmación no coincide con la nueva contraseña. ";
        }
        if (errores.isEmpty() && nueva.equals(actual)) {
            errores += "La nueva contraseña debe ser distinta de la actual. ";
        }

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());
            return "../password/edit";
        }

        usuariosService.cambiarPassword(usuario, nueva);
        model.addAttribute("ok", "La contraseña se actualizó correctamente.");
        return "../password/edit";
    }
}
