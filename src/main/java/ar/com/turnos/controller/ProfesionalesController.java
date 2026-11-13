package ar.com.turnos.controller;

import org.springframework.validation.BindingResult;
import ar.com.turnos
.entities.Profesionales;
import ar.com.turnos
.services.IEspecialidadesService;
import ar.com.turnos
.services.IProfesionalesService;
import ar.com.turnos
.services.ITipoDocsService;
import ar.com.turnos
.services.IUsuariosService;
import ar.com.turnos
.utiles.PageWrapper;
import ar.com.turnos
.utiles.Utiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ProfesionalesController {

    @Autowired
    IProfesionalesService entityService;

    @Autowired
    ITipoDocsService tipodocsService;
    @Autowired
    IEspecialidadesService especialidadesService;
    @Autowired
    IUsuariosService usuariosService;


    @RequestMapping(value = "/profesionales", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../profesionales/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<Profesionales> pagina = entityService.findAll(pageable);
        PageWrapper<Profesionales> page = new PageWrapper<Profesionales>(pagina, "/profesionales");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new Profesionales());
        cargarCombos(model);
    }

    @RequestMapping("/profesionales/refresh")
    public String refresh() {
        return "redirect:/profesionales";
    }

    @RequestMapping(value = "/profesionales/search", method = RequestMethod.POST)
    public String search(Model model, Profesionales entity) {
        if (Utiles.vacio(entity.getApellido())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getApellido()));
        model.addAttribute("entity", new Profesionales());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../profesionales/index";
    }

    @RequestMapping("/profesionales/create")
    public String create(Model model) {
        Profesionales entity = new Profesionales();
        entity.setTipodoc(tipodocsService.findDefecto());
        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../profesionales/edit";
    }

    @RequestMapping("/profesionales/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Profesionales entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../profesionales/edit";
    }

    @RequestMapping(value = "/profesionales", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Profesionales entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getApellido())) errores += "Apellido incorrecto. ";
        if (Utiles.vacio(entity.getNombre())) errores += "Nombre incorrecto. ";
        if (entity.getTipodoc() == null) errores += "Debe seleccionar un tipo de documento. ";
        if (Utiles.vacio(entity.getDni())) errores += "Nro. de documento incorrecto. ";
        if (Utiles.vacio(entity.getMatricula())) errores += "Matrícula incorrecta. ";
        if (entity.getEspecialidad() == null) errores += "Debe seleccionar una especialidad. ";

        // No se repite tipo + nro. de documento
        if (entity.getTipodoc() != null && !Utiles.vacio(entity.getDni())) {
            Profesionales p = entityService.findByDocumento(entity.getTipodoc().getId(), entity.getDni().trim());
            if (p != null && !p.getId().equals(entity.getId())) {
                errores += "Ya existe un profesional con ese documento. ";
            }
        }
        // Un usuario no puede estar asignado a dos profesionales
        if (entity.getUsuario() != null) {
            Profesionales p = entityService.findByUsuario(entity.getUsuario().getId());
            if (p != null && !p.getId().equals(entity.getId())) {
                errores += "El usuario ya está asignado a otro profesional. ";
            }
        }

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../profesionales/edit";
        }
        entity.setDni(entity.getDni().trim());
        entityService.save(entity);
        return "redirect:/profesionales";
    }

    @RequestMapping("/profesionales/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            Profesionales entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/profesionales";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../profesionales/index";
        }
    }

    private void cargarCombos(Model model) {
        model.addAttribute("tiposdocs", tipodocsService.getAll());
        model.addAttribute("especialidades", especialidadesService.getAll());
        model.addAttribute("usuarios", usuariosService.getAll());
    }
}
