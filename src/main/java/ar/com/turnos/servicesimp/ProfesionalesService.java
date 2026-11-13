package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IProfesionalesDao;
import ar.com.turnos
.entities.Profesionales;
import ar.com.turnos
.services.IProfesionalesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesionalesService implements IProfesionalesService {

    @Autowired
    private IProfesionalesDao entityDao;


    public List<Profesionales> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "apellido"));
    }

    public Page<Profesionales> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "apellido")));
    }

    public List<Profesionales> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public Profesionales get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(Profesionales entity) {
        entityDao.save(entity);
    }

    @Transactional
    public void delete(Profesionales entity) {
        entityDao.delete(entity);
    }

    public Profesionales findByDocumento(Integer tipodocId, String dni) {
        return entityDao.findOneByTipodocIdAndDni(tipodocId, dni);
    }

    public Profesionales findByUsuario(Integer usuarioId) {
        return entityDao.findOneByUsuarioId(usuarioId);
    }

}
