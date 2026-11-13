package ar.com.turnos.services;

import ar.com.turnos.entities.ObrasSociales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IObrasSocialesService {
    List<ObrasSociales> getAll();

    Page<ObrasSociales> findAll(Pageable pageable);

    List<ObrasSociales> findByDescrip(String descrip);

    ObrasSociales get(String id);

    void save(ObrasSociales entity);

    void delete(ObrasSociales entity);

}
