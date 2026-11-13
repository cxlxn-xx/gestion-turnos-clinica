package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IUsuariosDao;
import ar.com.turnos
.entities.Usuarios;
import ar.com.turnos
.services.IUsuariosService;
import ar.com.turnos
.utiles.Utiles;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosService implements IUsuariosService {

    @Autowired
    private IUsuariosDao entityDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<Usuarios> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "usuario"));
    }

    public Page<Usuarios> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "usuario")));
    }

    public List<Usuarios> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public Usuarios get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(Usuarios entity, String rawPassword) {
        if (!Utiles.vacio(rawPassword)) {
            entity.setContrasenia(passwordEncoder.encode(rawPassword));
        } else if (entity.getId() != null) {
            Usuarios actual = get(entity.getId());
            if (actual != null) entity.setContrasenia(actual.getContrasenia());
        }
        entityDao.save(entity);
    }

    @Transactional
    public void delete(Usuarios entity) {
        entityDao.delete(entity);
    }

    public Usuarios findByUsuario(String usuario) {
        return entityDao.findOneByUsuario(usuario);
    }

    public boolean passwordCoincide(Usuarios usuario, String rawPassword) {
        return usuario != null && rawPassword != null && passwordEncoder.matches(rawPassword, usuario.getContrasenia());
    }

    @Transactional
    public void cambiarPassword(Usuarios usuario, String nuevaPassword) {
        usuario.setContrasenia(passwordEncoder.encode(nuevaPassword));
        entityDao.save(usuario);
    }

}
