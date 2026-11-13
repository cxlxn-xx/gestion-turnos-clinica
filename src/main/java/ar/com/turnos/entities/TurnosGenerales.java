package ar.com.turnos.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "turnos_generales")
public class TurnosGenerales implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "fk_dia", referencedColumnName = "id", nullable = false)
    private Dias dia;

    @ManyToOne
    @JoinColumn(name = "fk_horario", referencedColumnName = "id", nullable = false)
    private Horarios horario;

    private boolean estado;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dias getDia() {
        return dia;
    }

    public void setDia(Dias dia) {
        this.dia = dia;
    }

    public Horarios getHorario() {
        return horario;
    }

    public void setHorario(Horarios horario) {
        this.horario = horario;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}