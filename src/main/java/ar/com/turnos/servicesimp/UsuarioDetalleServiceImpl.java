package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IUsuariosDao;
import ar.com.turnos
.entities.Usuarios;
import ar.com.turnos
.security.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UsuarioDetalleServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuariosDao dao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        final Usuarios retrievedUser = dao.findOneByUsuario(userName);

        if (retrievedUser == null) {
            throw new UsernameNotFoundException("Usuario o Password Inválido(s)");
        }

        return UserDetailsMapper.build(retrievedUser);
    }
}
