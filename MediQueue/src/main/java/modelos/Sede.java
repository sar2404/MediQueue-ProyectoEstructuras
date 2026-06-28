package modelos;

import estructuras.Lista;

// sede hospitalaria con sus salas, consultorios y usuarios
public class Sede {

    private String nombre;
    private Lista<Sala> salas;
    private Lista<Consultorio> consultorios;
    private Lista<Usuario> usuarios;

    public Sede() {

        this.salas = new Lista<>();
        this.consultorios = new Lista<>();
        this.usuarios = new Lista<>();
    }

    public Sede(String nombre) {

        this();
        this.nombre = nombre;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public Lista<Sala> getSalas() {

        return salas;
    }

    public Lista<Consultorio> getConsultorios() {

        return consultorios;
    }

    public Lista<Usuario> getUsuarios() {

        return usuarios;
    }
}
