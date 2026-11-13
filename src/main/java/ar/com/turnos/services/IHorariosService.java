package ar.com.turnos.services;

import ar.com.turnos.entities.Horarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHorariosService {
    List<Horarios> getAll();

    Page<Horarios> findAll(Pageable pageable);

    List<Horarios> findByDescrip(String descrip);

    Horarios get(Integer id);

    void save(Horarios entity);

    void delete(Horarios entity);

}
