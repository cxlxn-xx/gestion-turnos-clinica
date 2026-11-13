package ar.com.turnos.services;

import ar.com.turnos.entities.Especialidades;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEspecialidadesService {
    List<Especialidades> getAll();

    Page<Especialidades> findAll(Pageable pageable);

    List<Especialidades> findByDescrip(String descrip);

    Especialidades get(Integer id);

    void save(Especialidades entity);

    void delete(Especialidades entity);

}
