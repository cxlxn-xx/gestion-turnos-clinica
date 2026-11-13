package ar.com.turnos.servicesimp;

import ar.com.turnos
.dao.IDiasDao;
import ar.com.turnos
.dao.IHorariosDao;
import ar.com.turnos
.dao.ITurnosGeneralesDao;
import ar.com.turnos
.entities.Dias;
import ar.com.turnos
.entities.Horarios;
import ar.com.turnos
.entities.TurnosGenerales;
import ar.com.turnos
.services.ITurnosGeneralesService;
import ar.com.turnos
.utiles.Utiles;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TurnosGeneralesService implements ITurnosGeneralesService {

    @Autowired
    private ITurnosGeneralesDao entityDao;

    @Autowired
    private IDiasDao diasDao;
    @Autowired
    private IHorariosDao horariosDao;


    public List<TurnosGenerales> getAll() {
        return entityDao.findAll(Sort.by(Sort.Direction.ASC, "dia.id", "horario.descrip"));
    }

    public Page<TurnosGenerales> findAll(Pageable pageable) {
        return entityDao.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dia.id", "horario.descrip")));
    }

    public List<TurnosGenerales> findByDescrip(String descrip) {
        return entityDao.findByDescrip("%" + descrip.trim() + "%");
    }

    public TurnosGenerales get(String id) {
        return entityDao.findById(id).orElse(null);
    }

    @Transactional
    public void save(TurnosGenerales entity) {
        if (Utiles.vacio(entity.getId())) {
            entity.setId(Utiles.getUUID());
        }
        entityDao.save(entity);
    }

    @Transactional
    public void delete(TurnosGenerales entity) {
        entityDao.delete(entity);
    }

    public TurnosGenerales findByDiaHorario(Integer diaId, Integer horarioId) {
        return entityDao.findOneByDiaIdAndHorarioId(diaId, horarioId);
    }

    public List<TurnosGenerales> findByDia(Integer diaId) {
        return entityDao.findByDiaIdOrderByHorarioDescripAsc(diaId);
    }

    @Transactional
    public int generar(Integer diaId) {
        List<Dias> dias;
        if (diaId == null) {
            dias = diasDao.findAll();
        } else {
            Dias d = diasDao.findById(diaId).orElse(null);
            dias = d == null ? new ArrayList<Dias>() : List.of(d);
        }
        List<Horarios> horarios = horariosDao.findAll();

        int creados = 0;
        for (Dias d : dias) {
            for (Horarios h : horarios) {
                if (entityDao.findOneByDiaIdAndHorarioId(d.getId(), h.getId()) == null) {
                    TurnosGenerales t = new TurnosGenerales();
                    t.setId(Utiles.getUUID());
                    t.setDia(d);
                    t.setHorario(h);
                    t.setEstado(true);
                    entityDao.save(t);
                    creados++;
                }
            }
        }
        return creados;
    }

}