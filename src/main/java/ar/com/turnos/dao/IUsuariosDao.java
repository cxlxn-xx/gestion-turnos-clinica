package ar.com.turnos.dao;

import ar.com.turnos
.entities.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsuariosDao extends JpaRepository<Usuarios, Integer> {

    @Query("select c from Usuarios c where c.usuario like ?1 or c.descrip like ?1")
    List<Usuarios> findByDescrip(String name);

    Page<Usuarios> findAll(Pageable pageable);

    Usuarios findOneByUsuario(String usuario);

}
