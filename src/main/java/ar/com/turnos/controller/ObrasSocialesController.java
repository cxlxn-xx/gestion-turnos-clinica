package ar.com.turnos.controller;


import ar.com.turnos
.entities.ObrasSociales;
import ar.com.turnos
.services.IObrasSocialesService;
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
import org.springframework.web.bind.annotation.*;

@Controller
public class ObrasSocialesController {

    @Autowired
    IObrasSocialesService entityService;


    @RequestMapping(value = "/obrassociales", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../obrassociales/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<ObrasSociales> pagina = entityService.findAll(pageable);
        PageWrapper<ObrasSociales> page = new PageWrapper<ObrasSociales>(pagina, "/obrassociales");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new ObrasSociales());
        cargarCombos(model);
    }

    @RequestMapping("/obrassociales/refresh")
    public String refresh() {
        return "redirect:/obrassociales";
    }

    @RequestMapping(value = "/obrassociales/search", method = RequestMethod.POST)
    public String search(Model model, ObrasSociales entity) {
        if (Utiles.vacio(entity.getNombre())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getNombre()));
        model.addAttribute("entity", new ObrasSociales());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../obrassociales/index";
    }

    @RequestMapping("/obrassociales/create")
    public String create(Model model) {
        ObrasSociales entity = new ObrasSociales();
        model.addAttribute("nuevo", true);
        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../obrassociales/edit";
    }

    @RequestMapping("/obrassociales/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        ObrasSociales entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }
        model.addAttribute("nuevo", false);
        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../obrassociales/edit";
    }

    @RequestMapping(value = "/obrassociales", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") ObrasSociales entity, BindingResult result, @RequestParam(required = false) boolean nuevo) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (!Utiles.vacio(entity.getCodigo())) entity.setCodigo(entity.getCodigo().trim().toUpperCase());
        if (Utiles.vacio(entity.getCodigo())) {
            errores += "Código incorrecto. ";
        } else if (nuevo && entityService.get(entity.getCodigo()) != null) {
            errores += "El código ya existe. ";
        }
        if (Utiles.vacio(entity.getNombre())) errores += "Nombre incorrecto. ";
        if (!Utiles.vacio(entity.getEmail()) && !entity.getEmail().contains("@")) errores += "Email incorrecto. ";

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());
            model.addAttribute("nuevo", nuevo);
            cargarCombos(model);
            return "../obrassociales/edit";
        }

        entityService.save(entity);
        return "redirect:/obrassociales";
    }

    @RequestMapping("/obrassociales/delete/{id}")
    public String delete(@PathVariable String id, Model model, Pageable pageable) {
        try {
            ObrasSociales entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/obrassociales";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../obrassociales/index";
        }
    }

    private void cargarCombos(Model model) {

    }
}
