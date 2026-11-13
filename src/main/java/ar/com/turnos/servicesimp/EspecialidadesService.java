package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IEspecialidadesDao;
import ar.com.turnos
.entities.Especialidades;
import ar.com.turnos
.services.IEspecialidadesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadesService implements IEspecialidadesService {

    @Autowired
    private IEspecialidadesDao entityDao;


    public List<Especialidades> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "especialidad"));
    }

    public Page<Especialidades> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "especialidad")));
    }

    public List<Especialidades> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public Especialidades get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(Especialidades entity) {
        entityDao.save(entity);
    }

    @Transactional
    public void delete(Especialidades entity) {
        entityDao.delete(entity);
    }

}
