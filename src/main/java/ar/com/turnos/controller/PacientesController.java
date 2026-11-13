package ar.com.turnos.controller;

import ar.com.turnos
.entities.Pacientes;
import ar.com.turnos
.services.IPacientesService;
import ar.com.turnos
.services.ITipoDocsService;
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
public class PacientesController {

    @Autowired
    IPacientesService entityService;

    @Autowired
    ITipoDocsService tipodocsService;


    @RequestMapping(value = "/pacientes", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../pacientes/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<Pacientes> pagina = entityService.findAll(pageable);
        PageWrapper<Pacientes> page = new PageWrapper<Pacientes>(pagina, "/pacientes");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new Pacientes());
        cargarCombos(model);
    }

    @RequestMapping("/pacientes/refresh")
    public String refresh() {
        return "redirect:/pacientes";
    }

    @RequestMapping(value = "/pacientes/search", method = RequestMethod.POST)
    public String search(Model model, Pacientes entity) {
        if (Utiles.vacio(entity.getApellido())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getApellido()));
        model.addAttribute("entity", new Pacientes());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../pacientes/index";
    }

    @RequestMapping("/pacientes/create")
    public String create(Model model) {
        Pacientes entity = new Pacientes();
        entity.setTipodoc(tipodocsService.findDefecto());
        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../pacientes/edit";
    }

    @RequestMapping("/pacientes/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Pacientes entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../pacientes/edit";
    }

    @RequestMapping(value = "/pacientes", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Pacientes entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getApellido())) errores += "Apellido incorrecto. ";
        if (Utiles.vacio(entity.getNombre())) errores += "Nombre incorrecto. ";
        if (entity.getTipodoc() == null) errores += "Debe seleccionar un tipo de documento. ";
        if (Utiles.vacio(entity.getDni())) errores += "Nro. de documento incorrecto. ";
        if (!Utiles.vacio(entity.getEmail()) && !entity.getEmail().contains("@")) errores += "Email incorrecto. ";

        // No se repite tipo + nro. de documento
        if (entity.getTipodoc() != null && !Utiles.vacio(entity.getDni())) {
            Pacientes p = entityService.findByDocumento(entity.getTipodoc().getId(), entity.getDni().trim());
            if (p != null && !p.getId().equals(entity.getId())) {
                errores += "Ya existe un paciente con ese documento. ";
            }
        }

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../pacientes/edit";
        }
        entity.setDni(entity.getDni().trim());
        entityService.save(entity);
        return "redirect:/pacientes";
    }

    @RequestMapping("/pacientes/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            Pacientes entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/pacientes";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../pacientes/index";
        }
    }

    private void cargarCombos(Model model) {
        model.addAttribute("tiposdocs", tipodocsService.getAll());
    }
}
