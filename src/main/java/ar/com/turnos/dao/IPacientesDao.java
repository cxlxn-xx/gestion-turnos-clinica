package ar.com.turnos.dao;

import ar.com.turnos
.entities.Pacientes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPacientesDao extends JpaRepository<Pacientes, Integer> {

    @Query("select c from Pacientes c where c.apellido like ?1 or c.nombre like ?1 or c.dni like ?1")
    List<Pacientes> findByDescrip(String name);

    Page<Pacientes> findAll(Pageable pageable);

    Pacientes findOneByTipodocIdAndDni(Integer tipodocId, String dni);

}
