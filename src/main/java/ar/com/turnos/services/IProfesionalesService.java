package ar.com.turnos.services;

import ar.com.turnos.entities.Profesionales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProfesionalesService {
    List<Profesionales> getAll();

    Page<Profesionales> findAll(Pageable pageable);

    List<Profesionales> findByDescrip(String descrip);

    Profesionales get(Integer id);

    void save(Profesionales entity);

    void delete(Profesionales entity);

    Profesionales findByDocumento(Integer tipodocId, String dni);

    Profesionales findByUsuario(Integer usuarioId);

}
