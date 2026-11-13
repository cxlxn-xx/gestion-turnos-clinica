package ar.com.turnos.servicesimp;

import ar.com.turnos.dao.IDiasDao;
import ar.com.turnos.entities.Dias;
import ar.com.turnos.services.IDiasService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiasService implements IDiasService {

    @Autowired
    private IDiasDao entityDao;


    public List<Dias> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Page<Dias> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id")));
    }

    public List<Dias> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public Dias get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(Dias entity) {
        entityDao.save(entity);
    }

    @Transactional
    public void delete(Dias entity) {
        entityDao.delete(entity);
    }

}
