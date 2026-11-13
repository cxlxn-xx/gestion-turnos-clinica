package ar.com.turnos.security;

import ar.com.turnos.entities.Usuarios;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

public class UserDetailsMapper {

    public static final int NIVEL_ADMINISTRADOR = 1;
    public static final int NIVEL_PROFESIONAL = 2;

    public static UserDetails build(Usuarios user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsuario(), user.getContrasenia(), getAuthorities(user));
    }

    /**
     * El perfil sale del nivel de acceso del rol del usuario:
     * 1 = Administrador, 2 = Profesional, otro valor = Usuario (solo cambio de contraseña).
     */
    private static Set<? extends GrantedAuthority> getAuthorities(Usuarios user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        int nivel = user.getRol() == null ? 0 : user.getRol().getNivelacceso();
        if (nivel == NIVEL_ADMINISTRADOR) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        } else if (nivel == NIVEL_PROFESIONAL) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESIONAL"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO"));
        }
        return authorities;
    }
}