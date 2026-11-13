package ar.com.turnos.config;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Deja disponibles en todas las páginas el usuario logueado y su perfil (para armar el menú). */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("usuarioActual")
    public String usuarioActual(Authentication auth) {
        return auth != null ? auth.getName() : null;
    }

    @ModelAttribute("esAdmin")
    public boolean esAdmin(Authentication auth) {
        return tiene(auth, "ROLE_ADMINISTRADOR");
    }

    @ModelAttribute("esProfesional")
    public boolean esProfesional(Authentication auth) {
        return tiene(auth, "ROLE_PROFESIONAL");
    }

    private boolean tiene(Authentication auth, String authority) {
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
    }
}
