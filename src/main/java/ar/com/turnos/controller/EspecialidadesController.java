package ar.com.turnos.controller;


import ar.com.turnos
.entities.Especialidades;
import ar.com.turnos
.services.IEspecialidadesService;
import ar.com.turnos
.utiles.PageWrapper;
import ar.com.turnos
.utiles.Utiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class EspecialidadesController {

    @Autowired
    IEspecialidadesService entityService;


    @RequestMapping(value = "/especialidades", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../especialidades/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<Especialidades> pagina = entityService.findAll(pageable);
        PageWrapper<Especialidades> page = new PageWrapper<Especialidades>(pagina, "/especialidades");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new Especialidades());
        cargarCombos(model);
    }

    @RequestMapping("/especialidades/refresh")
    public String refresh() {
        return "redirect:/especialidades";
    }

    @RequestMapping(value = "/especialidades/search", method = RequestMethod.POST)
    public String search(Model model, Especialidades entity) {
        if (Utiles.vacio(entity.getEspecialidad())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getEspecialidad()));
        model.addAttribute("entity", new Especialidades());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../especialidades/index";
    }

    @RequestMapping("/especialidades/create")
    public String create(Model model) {
        Especialidades entity = new Especialidades();

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../especialidades/edit";
    }

    @RequestMapping("/especialidades/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Especialidades entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../especialidades/edit";
    }

    @RequestMapping(value = "/especialidades", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Especialidades entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getEspecialidad())) errores += "Especialidad incorrecta. ";

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../especialidades/edit";
        }

        entityService.save(entity);
        return "redirect:/especialidades";
    }

    @RequestMapping("/especialidades/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            Especialidades entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/especialidades";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../especialidades/index";
        }
    }

    private void cargarCombos(Model model) {

    }
}
