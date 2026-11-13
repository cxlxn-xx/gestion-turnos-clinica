package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.ITipoDocsDao;
import ar.com.turnos
.entities.TipoDocs;
import ar.com.turnos
.services.ITipoDocsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocsService implements ITipoDocsService {

    @Autowired
    private ITipoDocsDao entityDao;


    public List<TipoDocs> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "descrip"));
    }

    public Page<TipoDocs> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "descrip")));
    }

    public List<TipoDocs> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public TipoDocs get(Integer id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(TipoDocs entity) {
        entityDao.save(entity);

        // Si este queda como documento por defecto, se desmarcan los demás
        if (entity.isPordefecto()) {
            List<TipoDocs> entities = entityDao.findAll();
            for (TipoDocs d : entities) {
                d.setPordefecto(d.getId().equals(entity.getId()));
            }
            entityDao.saveAll(entities);
        }
    }

    @Transactional
    public void delete(TipoDocs entity) {
        entityDao.delete(entity);
    }

    public TipoDocs findDefecto() {
        return entityDao.findFirstByPordefectoTrue();
    }

}
