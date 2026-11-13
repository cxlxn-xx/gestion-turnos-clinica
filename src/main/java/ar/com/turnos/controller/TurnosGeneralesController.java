package ar.com.turnos.controller;

import ar.com.turnos.entities.TurnosGenerales;
import ar.com.turnos.services.IDiasService;
import ar.com.turnos.services.IHorariosService;
import ar.com.turnos.services.ITurnosGeneralesService;
import ar.com.turnos.utiles.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TurnosGeneralesController {

    @Autowired
    ITurnosGeneralesService entityService;

    @Autowired
    IDiasService diasService;
    @Autowired
    IHorariosService horariosService;


    @RequestMapping(value = "/turnosgenerales", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../turnosgenerales/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<TurnosGenerales> pagina = entityService.findAll(pageable);
        PageWrapper<TurnosGenerales> page = new PageWrapper<TurnosGenerales>(pagina, "/turnosgenerales");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new TurnosGenerales());
        cargarCombos(model);
    }

    @RequestMapping("/turnosgenerales/refresh")
    public String refresh() {
        return "redirect:/turnosgenerales";
    }

    @RequestMapping("/turnosgenerales/create")
    public String create(Model model) {
        TurnosGenerales entity = new TurnosGenerales();
        entity.setEstado(true);
        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../turnosgenerales/edit";
    }

    @RequestMapping("/turnosgenerales/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        TurnosGenerales entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../turnosgenerales/edit";
    }

    @RequestMapping(value = "/turnosgenerales", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") TurnosGenerales entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (entity.getDia() == null) errores += "Debe seleccionar un día. ";
        if (entity.getHorario() == null) errores += "Debe seleccionar un horario. ";
        if (entity.getDia() != null && entity.getHorario() != null) {
            TurnosGenerales t = entityService.findByDiaHorario(entity.getDia().getId(), entity.getHorario().getId());
            if (t != null && !t.getId().equals(entity.getId())) {
                errores += "Ya existe ese día y horario. ";
            }
        }

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../turnosgenerales/edit";
        }

        entityService.save(entity);
        return "redirect:/turnosgenerales";
    }

    @RequestMapping("/turnosgenerales/delete/{id}")
    public String delete(@PathVariable String id, Model model, Pageable pageable) {
        try {
            TurnosGenerales entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/turnosgenerales";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../turnosgenerales/index";
        }
    }

    // Crea los turnos generales (día x horario) que falten
    @RequestMapping(value = "/turnosgenerales/generar", method = RequestMethod.POST)
    public String generar(@RequestParam(required = false) Integer dia, RedirectAttributes redirect) {
        int creados = entityService.generar(dia);
        redirect.addFlashAttribute("message", "Turnos generales creados: " + creados);
        return "redirect:/turnosgenerales";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("dias", diasService.getAll());
        model.addAttribute("horarios", horariosService.getAll());
    }
}
