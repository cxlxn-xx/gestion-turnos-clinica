package ar.com.turnos.utiles;

import java.util.UUID;

public class Utiles {

    private Utiles() {
    }

    /** true si el texto es null o está vacío / solo espacios */
    public static boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
