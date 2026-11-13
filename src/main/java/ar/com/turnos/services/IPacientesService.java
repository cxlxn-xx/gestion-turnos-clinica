package ar.com.turnos.services;

import ar.com.turnos.entities.Pacientes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPacientesService {
    List<Pacientes> getAll();

    Page<Pacientes> findAll(Pageable pageable);

    List<Pacientes> findByDescrip(String descrip);

    Pacientes get(Integer id);

    void save(Pacientes entity);

    void delete(Pacientes entity);

    Pacientes findByDocumento(Integer tipodocId, String dni);

}
