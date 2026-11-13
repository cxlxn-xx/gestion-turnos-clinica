package ar.com.turnos.controller;

import ar.com.turnos
.entities.Usuarios;
import ar.com.turnos
.services.IRolesService;
import ar.com.turnos.services.IUsuariosService;
import ar.com.turnos.utiles.PageWrapper;
import ar.com.turnos.utiles.Utiles;
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
public class UsuariosController {

    @Autowired
    IUsuariosService entityService;

    @Autowired
    IRolesService rolesService;


    @RequestMapping(value = "/usuarios", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        cargarLista(model, pageable);
        return "../usuarios/index";
    }

    private void cargarLista(Model model, Pageable pageable) {
        Page<Usuarios> pagina = entityService.findAll(pageable);
        PageWrapper<Usuarios> page = new PageWrapper<Usuarios>(pagina, "/usuarios");
        model.addAttribute("entities", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("entity", new Usuarios());
        cargarCombos(model);
    }

    @RequestMapping("/usuarios/refresh")
    public String refresh() {
        return "redirect:/usuarios";
    }

    @RequestMapping(value = "/usuarios/search", method = RequestMethod.POST)
    public String search(Model model, Usuarios entity) {
        if (Utiles.vacio(entity.getUsuario())) {
            return refresh();
        }
        model.addAttribute("entities", entityService.findByDescrip(entity.getUsuario()));
        model.addAttribute("entity", new Usuarios());
        model.addAttribute("page", null);
        cargarCombos(model);
        return "../usuarios/index";
    }

    @RequestMapping("/usuarios/create")
    public String create(Model model) {
        Usuarios entity = new Usuarios();

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../usuarios/edit";
    }

    @RequestMapping("/usuarios/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Usuarios entity = entityService.get(id);
        if (entity == null) {
            return refresh();
        }

        model.addAttribute("entity", entity);
        cargarCombos(model);
        return "../usuarios/edit";
    }

    @RequestMapping(value = "/usuarios", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("entity") Usuarios entity, BindingResult result) {
        String errores = "";
        if (result.hasErrors()) errores += "Hay datos con formato incorrecto. ";
        if (Utiles.vacio(entity.getUsuario())) {
            errores += "Usuario incorrecto. ";
        } else {
            Usuarios u = entityService.findByUsuario(entity.getUsuario().trim());
            if (u != null && !u.getId().equals(entity.getId())) {
                errores += "El nombre de usuario ya existe. ";
            }
        }
        // En un usuario nuevo la contraseña es obligatoria; al editar, vacía = no se cambia
        if (entity.getId() == null && Utiles.vacio(entity.getContrasenia())) errores += "Contraseña incorrecta. ";
        if (entity.getRol() == null) errores += "Debe seleccionar un rol. ";

        if (!errores.isEmpty()) {
            model.addAttribute("message", errores.trim());

            cargarCombos(model);
            return "../usuarios/edit";
        }
        entity.setUsuario(entity.getUsuario().trim());
        String rawPassword = entity.getContrasenia();
        entity.setContrasenia(null);
        entityService.save(entity, rawPassword);
        return "redirect:/usuarios";
    }

    @RequestMapping("/usuarios/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Pageable pageable) {
        try {
            Usuarios entity = entityService.get(id);
            if (entity != null) {
                entityService.delete(entity);
            }
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("message", "No se puede eliminar el registro: está siendo utilizado por otros datos.");
            cargarLista(model, pageable);
            return "../usuarios/index";
        }
    }

    private void cargarCombos(Model model) {
        model.addAttribute("roles", rolesService.getAll());
    }
}
