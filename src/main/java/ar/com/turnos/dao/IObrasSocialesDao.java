package ar.com.turnos.dao;

import ar.com.turnos
.entities.ObrasSociales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IObrasSocialesDao extends JpaRepository<ObrasSociales, String> {

    @Query("select c from ObrasSociales c where c.nombre like ?1 or c.codigo like ?1")
    List<ObrasSociales> findByDescrip(String name);

    Page<ObrasSociales> findAll(Pageable pageable);

}