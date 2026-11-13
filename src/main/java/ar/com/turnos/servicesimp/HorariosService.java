package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IHorariosDao;
import ar.com.turnos
.entities.Horarios;
import ar.com.turnos
.services.IHorariosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorariosService implements IHorariosService {

    @Autowired
    private IHorariosDao entityDao;


    public List<Horarios> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "descrip"));
    }

    public Page<Horarios> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "descrip")));
    }

    public List<Horarios> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public Horarios get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(Horarios entity) {
        entityDao.save(entity);
    }

    @Transactional
    public void delete(Horarios entity) {
        entityDao.delete(entity);
    }

}
