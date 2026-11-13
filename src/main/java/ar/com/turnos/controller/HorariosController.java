package ar.com.turnos.controller;

import ar.com.turnos
.entities.Horarios;
import ar.com.turnos
.services.IHorariosService;
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
public class HorariosController {

    @Autowired
    IHorariosService entityService;


    @RequestMapping(value = "/horarios", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../horarios/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<Horarios> pagina = entityService.findAll(pageable);
        PageWrapper<Horarios> page = new PageWrapper<Horarios>(pagina, "/horarios");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new Horarios());
        cargarCombos(model);
    }

    @RequestMapping("/horarios/refresh")
    public String refresh() {
        return "redirect:/horarios";
    }

    @RequestMapping(value = "/horarios/search", method = RequestMethod.POST)
    public String search(Model model, Horarios entity) {
        if (Utiles.vacio(entity.getDescrip())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getDescrip()));
        model.addAttribute("entity", new Horarios());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../horarios/index";
    }

    @RequestMapping("/horarios/create")
    public String create(Model model) {
        Horarios entity = new Horarios();

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../horarios/edit";
    }

    @RequestMapping("/horarios/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Horarios entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../horarios/edit";
    }

    @RequestMapping(value = "/horarios", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Horarios entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getDescrip())) errores += "Descripción incorrecta. ";
        if (Utiles.vacio(entity.getMeridiano())) errores += "Meridiano incorrecto. ";

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../horarios/edit";
        }

        entityService.save(entity);
        return "redirect:/horarios";
    }

    @RequestMapping("/horarios/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            Horarios entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/horarios";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../horarios/index";
        }
    }

    private void cargarCombos(Model model) {

    }
}
