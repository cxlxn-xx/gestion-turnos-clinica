package ar.com.turnos.services;


import ar.com.turnos.entities.TipoDocs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITipoDocsService {
    List<TipoDocs> getAll();

    Page<TipoDocs> findAll(Pageable pageable);

    List<TipoDocs> findByDescrip(String descrip);

    TipoDocs get(Integer id);

    void save(TipoDocs entity);

    void delete(TipoDocs entity);

    TipoDocs findDefecto();

}
