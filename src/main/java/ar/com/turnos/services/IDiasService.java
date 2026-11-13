package ar.com.turnos.services;

import ar.com.turnos.entities.Dias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDiasService {
    List<Dias> getAll();

    Page<Dias> findAll(Pageable pageable);

    List<Dias> findByDescrip(String descrip);

    Dias get(Integer id);

    void save(Dias entity);

    void delete(Dias entity);

}
