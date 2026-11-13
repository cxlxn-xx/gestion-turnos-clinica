package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IRolesDao;
import ar.com.turnos
.entities.Roles;
import ar.com.turnos
.services.IRolesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService implements IRolesService {

    @Autowired
    private IRolesDao entityDao;


    public List<Roles> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "descrip"));
    }

    public Page<Roles> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "descrip")));
    }

    public List<Roles> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public Roles get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(Roles entity) {
        entityDao.save(entity);
    }

    @Transactional
    public void delete(Roles entity) {
        entityDao.delete(entity);
    }

}
