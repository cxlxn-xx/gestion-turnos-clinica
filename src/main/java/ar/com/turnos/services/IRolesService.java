package ar.com.turnos.services;

import ar.com.turnos.entities.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolesService {
    List<Roles> getAll();

    Page<Roles> findAll(Pageable pageable);

    List<Roles> findByDescrip(String descrip);

    Roles get(Integer id);

    void save(Roles entity);

    void delete(Roles entity);

}
