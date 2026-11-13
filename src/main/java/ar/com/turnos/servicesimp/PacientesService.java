package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IPacientesDao;
import ar.com.turnos
.entities.Pacientes;
import ar.com.turnos
.services.IPacientesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacientesService implements IPacientesService {

    @Autowired
    private IPacientesDao entityDao;


    public List<Pacientes> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "apellido"));
    }

    public Page<Pacientes> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "apellido")));
    }

    public List<Pacientes> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public Pacientes get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(Pacientes entity) {
        entityDao.save(entity);
    }

    @Transactional
    public void delete(Pacientes entity) {
        entityDao.delete(entity);
    }

    public Pacientes findByDocumento(Integer tipodocId, String dni) {
        return entityDao.findOneByTipodocIdAndDni(tipodocId, dni);
    }

}