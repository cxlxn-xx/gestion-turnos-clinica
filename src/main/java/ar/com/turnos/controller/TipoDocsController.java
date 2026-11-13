package ar.com.turnos.controller;



import ar.com.turnos
.entities.TipoDocs;
import ar.com.turnos
.services.ITipoDocsService;
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
public class TipoDocsController {

    @Autowired
    ITipoDocsService entityService;


    @RequestMapping(value = "/tipodocs", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../tipodocs/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<TipoDocs> pagina = entityService.findAll(pageable);
        PageWrapper<TipoDocs> page = new PageWrapper<TipoDocs>(pagina, "/tipodocs");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new TipoDocs());
        cargarCombos(model);
    }

    @RequestMapping("/tipodocs/refresh")
    public String refresh() {
        return "redirect:/tipodocs";
    }

    @RequestMapping(value = "/tipodocs/search", method = RequestMethod.POST)
    public String search(Model model, TipoDocs entity) {
        if (Utiles.vacio(entity.getDescrip())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getDescrip()));
        model.addAttribute("entity", new TipoDocs());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../tipodocs/index";
    }

    @RequestMapping("/tipodocs/create")
    public String create(Model model) {
        TipoDocs entity = new TipoDocs();

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../tipodocs/edit";
    }

    @RequestMapping("/tipodocs/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        TipoDocs entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../tipodocs/edit";
    }

    @RequestMapping(value = "/tipodocs", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") TipoDocs entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getDescrip())) errores += "Descripción incorrecta. ";

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../tipodocs/edit";
        }

        entityService.save(entity);
        return "redirect:/tipodocs";
    }

    @RequestMapping("/tipodocs/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            TipoDocs entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/tipodocs";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../tipodocs/index";
        }
    }

    private void cargarCombos(Model model) {

    }
}
