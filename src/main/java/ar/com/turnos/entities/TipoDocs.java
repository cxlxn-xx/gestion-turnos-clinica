package ar.com.turnos.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tipodocs")
public class TipoDocs implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descrip;

    private boolean pordefecto;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public boolean isPordefecto() {
        return pordefecto;
    }

    public void setPordefecto(boolean pordefecto) {
        this.pordefecto = pordefecto;
    }
}
