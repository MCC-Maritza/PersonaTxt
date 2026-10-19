package persona;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *
 * @author maryse
 */
// Clase que representa el objeto que guardaremos en el archivo binario
// ES OBLIGATORIO que implemente Serializable
class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private int edad;
    private String correo;

    public Persona(String nombre, int edad, String correo) {
        this.nombre = nombre;
        this.edad = edad;
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Edad: " + edad + " | Correo: " + correo;
    }

    public static void main(String[] args) {
        new CreadorBinarioGUI().setVisible(true);
    }
}
