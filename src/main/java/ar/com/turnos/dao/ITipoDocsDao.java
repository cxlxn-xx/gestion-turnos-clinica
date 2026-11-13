package ar.com.turnos.dao;

import ar.com.turnos
.entities.TipoDocs;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoDocsDao extends JpaRepository<TipoDocs, Integer> {

    @Query("select c from TipoDocs c where c.descrip like ?1")
    List<TipoDocs> findByDescrip(String name);

    Page<TipoDocs> findAll(Pageable pageable);

    TipoDocs findFirstByPordefectoTrue();
}
