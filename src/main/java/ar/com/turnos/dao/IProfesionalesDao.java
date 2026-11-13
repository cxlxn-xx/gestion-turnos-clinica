package ar.com.turnos.dao;

import ar.com.turnos
.entities.Profesionales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProfesionalesDao extends JpaRepository<Profesionales, Integer> {

    @Query("select c from Profesionales c where c.apellido like ?1 or c.nombre like ?1 or c.dni like ?1 or c.matricula like ?1")
    List<Profesionales> findByDescrip(String name);

    Page<Profesionales> findAll(Pageable pageable);

    Profesionales findOneByTipodocIdAndDni(Integer tipodocId, String dni);

    Profesionales findOneByUsuarioId(Integer usuarioId);

}