package ar.com.turnos.config;

import ar.com.turnos.dao.IRolesDao;
import ar.com.turnos.dao.IUsuariosDao;
import ar.com.turnos.entities.Roles;
import ar.com.turnos.entities.Usuarios;
import ar.com.turnos.security.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUsuariosDao usuariosDao;
    @Autowired
    private IRolesDao rolesDao;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (usuariosDao.count() > 0) {
            return;
        }

        // 1. Crear o buscar el Rol de Administrador
        Roles rolAdmin = rolesDao.findFirstByNivelacceso(UserDetailsMapper.NIVEL_ADMINISTRADOR);
        if (rolAdmin == null) {
            rolAdmin = new Roles();
            rolAdmin.setDescrip("Administrador");
            rolAdmin.setNivelacceso(UserDetailsMapper.NIVEL_ADMINISTRADOR);
            rolAdmin = rolesDao.save(rolAdmin);
        }


        Usuarios admin = new Usuarios();
        admin.setUsuario("admin");


        admin.setContrasenia(encoder.encode("admin123"));

        admin.setDescrip("Administrador del Sistema");


        admin.setRol(rolAdmin);

        // Guardamos el usuario en la base de datos
        usuariosDao.save(admin);
    }
}