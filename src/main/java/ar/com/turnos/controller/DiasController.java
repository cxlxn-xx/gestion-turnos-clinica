package ar.com.turnos.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import ar.com.turnos
.entities.Dias;
import ar.com.turnos
.services.IDiasService;
import ar.com.turnos
.utiles.PageWrapper;
import ar.com.turnos
.utiles.Utiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DiasController {

    @Autowired
    IDiasService entityService;


    @RequestMapping(value = "/dias", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../dias/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<Dias> pagina = entityService.findAll(pageable);
        PageWrapper<Dias> page = new PageWrapper<Dias>(pagina, "/dias");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new Dias());
        cargarCombos(model);
    }

    @RequestMapping("/dias/refresh")
    public String refresh() {
        return "redirect:/dias";
    }

    @RequestMapping(value = "/dias/search", method = RequestMethod.POST)
    public String search(Model model, Dias entity) {
        if (Utiles.vacio(entity.getDescrip())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getDescrip()));
        model.addAttribute("entity", new Dias());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../dias/index";
    }

    @RequestMapping("/dias/create")
    public String create(Model model) {
        Dias entity = new Dias();

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../dias/edit";
    }

    @RequestMapping("/dias/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Dias entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../dias/edit";
    }

    @RequestMapping(value = "/dias", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Dias entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getDescrip())) errores += "Descripción incorrecta. ";
        if (Utiles.vacio(entity.getAbreviatura())) errores += "Abreviatura incorrecta. ";

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../dias/edit";
        }

        entityService.save(entity);
        return "redirect:/dias";
    }

    @RequestMapping("/dias/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            Dias entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/dias";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../dias/index";
        }
    }

    private void cargarCombos(Model model) {

    }
}
