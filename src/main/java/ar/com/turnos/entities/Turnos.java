package ar.com.turnos.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
public class Turnos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "fk_turnogeneral", referencedColumnName = "id", nullable = false)
    private TurnosGenerales turnogeneral;

    @ManyToOne
    @JoinColumn(name = "fk_paciente", referencedColumnName = "id", nullable = false)
    private Pacientes paciente;

    @ManyToOne
    @JoinColumn(name = "fk_profesional", referencedColumnName = "id", nullable = false)
    private Profesionales profesional;

    private String descrip;

    private String estado;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechahora;

    @ManyToOne
    @JoinColumn(name = "fk_obrasocial", referencedColumnName = "codigo", nullable = false)
    private ObrasSociales obrasocial;

    @ManyToOne
    @JoinColumn(name = "fk_returno", referencedColumnName = "id", nullable = true)
    private Turnos returno;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TurnosGenerales getTurnogeneral() {
        return turnogeneral;
    }

    public void setTurnogeneral(TurnosGenerales turnogeneral) {
        this.turnogeneral = turnogeneral;
    }

    public Pacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(Pacientes paciente) {
        this.paciente = paciente;
    }

    public Profesionales getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesionales profesional) {
        this.profesional = profesional;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getFechahora() {
        return fechahora;
    }

    public void setFechahora(LocalDateTime fechahora) {
        this.fechahora = fechahora;
    }

    public ObrasSociales getObrasocial() {
        return obrasocial;
    }

    public void setObrasocial(ObrasSociales obrasocial) {
        this.obrasocial = obrasocial;
    }

    public Turnos getReturno() {
        return returno;
    }

    public void setReturno(Turnos returno) {
        this.returno = returno;
    }
}

