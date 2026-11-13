package ar.com.turnos.servicesimp;


import ar.com.turnos
.dao.IObrasSocialesDao;
import ar.com.turnos
.entities.ObrasSociales;
import ar.com.turnos
.services.IObrasSocialesService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ObrasSocialesService implements IObrasSocialesService {

    @Autowired
    private IObrasSocialesDao entityDao;

    public List<ObrasSociales> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "nombre"));
    }

    public Page<ObrasSociales> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "nombre")));
    }

    public List<ObrasSociales> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public ObrasSociales get(String id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(ObrasSociales entity) {
        entityDao.save(entity);
    }

    @Transactional
    public void delete(ObrasSociales entity) {
        entityDao.delete(entity);
    }
}