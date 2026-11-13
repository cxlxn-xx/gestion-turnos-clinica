package ar.com.turnos.services;

import ar.com.turnos.entities.TurnosGenerales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITurnosGeneralesService {
    List<TurnosGenerales> getAll();
    Page<TurnosGenerales> findAll(Pageable pageable);
    List<TurnosGenerales> findByDescrip(String descrip);
    TurnosGenerales get(String id);
    void save(TurnosGenerales entity);
    void delete(TurnosGenerales entity);
    TurnosGenerales findByDiaHorario(Integer diaId, Integer horarioId);

    List<TurnosGenerales> findByDia(Integer diaId);

    int generar(Integer diaId);

}
