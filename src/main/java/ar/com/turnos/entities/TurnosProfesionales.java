package ar.com.turnos.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "turnos_profesionales")
public class TurnosProfesionales implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "fk_profesional", referencedColumnName = "id", nullable = false)
    private Profesionales profesional;

    @ManyToOne
    @JoinColumn(name = "fk_turnogeneral", referencedColumnName = "id", nullable = false)
    private TurnosGenerales turnogeneral;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profesionales getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesionales profesional) {
        this.profesional = profesional;
    }

    public TurnosGenerales getTurnogeneral() {
        return turnogeneral;
    }

    public void setTurnogeneral(TurnosGenerales turnogeneral) {
        this.turnogeneral = turnogeneral;
    }
}