package ar.com.turnos.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "profesionales")
public class Profesionales implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String apellido;

    private String nombre;

    private String dni;

    @ManyToOne
    @JoinColumn(name = "fk_tipodoc", referencedColumnName = "id", nullable = false)
    private TipoDocs tipodoc;

    private String matricula;

    private String telefono;

    @ManyToOne
    @JoinColumn(name = "fk_especialidad", referencedColumnName = "id", nullable = false)
    private Especialidades especialidad;

    @OneToOne
    @JoinColumn(name = "fk_usuario", referencedColumnName = "id", nullable = true)
    private Usuarios usuario;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public TipoDocs getTipodoc() {
        return tipodoc;
    }

    public void setTipodoc(TipoDocs tipodoc) {
        this.tipodoc = tipodoc;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidades especialidad) {
        this.especialidad = especialidad;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
}
