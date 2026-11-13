package ar.com.turnos.controller;

import ar.com.turnos
.entities.Roles;
import ar.com.turnos
.services.IRolesService;
import ar.com.turnos
.utiles.PageWrapper;
import ar.com.turnos
.utiles.Utiles;
   import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RolesController {

    @Autowired
    IRolesService entityService;


    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../roles/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<Roles> pagina = entityService.findAll(pageable);
        PageWrapper<Roles> page = new PageWrapper<Roles>(pagina, "/roles");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new Roles());
        cargarCombos(model);
    }

    @RequestMapping("/roles/refresh")
    public String refresh() {
        return "redirect:/roles";
    }

    @RequestMapping(value = "/roles/search", method = RequestMethod.POST)
    public String search(Model model, Roles entity) {
        if (Utiles.vacio(entity.getDescrip())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getDescrip()));
        model.addAttribute("entity", new Roles());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../roles/index";
    }

    @RequestMapping("/roles/create")
    public String create(Model model) {
        Roles entity = new Roles();

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../roles/edit";
    }

    @RequestMapping("/roles/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Roles entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../roles/edit";
    }

    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Roles entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getDescrip())) errores += "Descripción incorrecta. ";
        if (entity.getNivelacceso() < 1) errores += "Nivel de acceso incorrecto (1 = Administrador, 2 = Profesional). ";

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../roles/edit";
        }

        entityService.save(entity);
        return "redirect:/roles";
    }

    @RequestMapping("/roles/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            Roles entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/roles";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../roles/index";
        }
    }

    private void cargarCombos(Model model) {

    }
}
