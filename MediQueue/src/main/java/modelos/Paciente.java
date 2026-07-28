package modelos;

import persistencia.Json;

/**
 *
 * @author XPC
 */

//Como lo hemos visto en clase, el profe realizó un arbol generico, por lo tanto cambié la declaración de la clase:
public class Paciente implements Comparable<Paciente> {

    private int id;
    private String nombre;
    private String identificacion;
    private int edad;
    private String tipoSeguro;

    public Paciente(int id, String nombre, String identificacion, int edad, String tipoSeguro) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.edad = edad;
        this.tipoSeguro = tipoSeguro;
    }

    public Paciente() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    @Override
    public String toString() {
        return "Paciente{" + "id=" + id + ", nombre='" + nombre + '\'' + ", identificacion='" + identificacion + '\'' + ", edad=" + edad + ", tipoSeguro='" + tipoSeguro + '\'' + '}';
    }

    
    //Agregué el compare To:
    @Override
    public int compareTo(Paciente otro) {
        return this.identificacion.compareTo(otro.identificacion);
    }

    // Serializacion manual (Modulo 1.1): la clase sabe escribirse y leerse como linea JSON.
    public String aTextoJson() {
        return "{ \"id\": " + id
                + ", \"nombre\": \"" + escapar(nombre) + "\""
                + ", \"identificacion\": \"" + escapar(identificacion) + "\""
                + ", \"edad\": " + edad
                + ", \"tipoSeguro\": \"" + escapar(tipoSeguro) + "\" }";
    }

    public static Paciente desdeTexto(String linea) {
        return new Paciente(
                Integer.parseInt(Json.valorDe(linea, "id")),
                Json.valorDe(linea, "nombre"),
                Json.valorDe(linea, "identificacion"),
                Integer.parseInt(Json.valorDe(linea, "edad")),
                Json.valorDe(linea, "tipoSeguro")
        );
    }

    private String escapar(String texto) {
        return texto == null ? "" : texto.replace("\"", "'");
    }

}
