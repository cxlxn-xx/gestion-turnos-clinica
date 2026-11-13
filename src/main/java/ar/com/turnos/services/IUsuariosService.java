package ar.com.turnos.services;

import ar.com.turnos.entities.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuariosService {
    List<Usuarios> getAll();

    Page<Usuarios> findAll(Pageable pageable);

    List<Usuarios> findByDescrip(String descrip);

    Usuarios get(Integer id);


    void save(Usuarios entity, String rawPassword);

    void delete(Usuarios entity);

    Usuarios findByUsuario(String usuario);


    boolean passwordCoincide(Usuarios usuario, String rawPassword);

    void cambiarPassword(Usuarios usuario, String nuevaPassword);

}
