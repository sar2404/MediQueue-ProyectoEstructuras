package modelos;

import persistencia.Json;

public class Tiquete {

    private int id;
    private Paciente paciente;
    private String fechaIngreso;
    private String fechaAtencion;
    private String prioridad;
    private String tipoAtencion;
    private String atencion;

    public Tiquete(int id, Paciente paciente, String fechaIngreso, String fechaAtencion, String prioridad, String tipoAtencion, String atencion) {
        this.id = id;
        this.paciente = paciente;
        this.fechaIngreso = fechaIngreso;
        this.fechaAtencion = fechaAtencion;
        this.prioridad = prioridad;
        this.tipoAtencion = tipoAtencion;
        this.atencion = atencion;
    }

    public Tiquete() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(String fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(String tipoAtencion) {
        this.tipoAtencion = tipoAtencion;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
    }

    @Override
    public String toString() {
        return "Tiquete #" + id + " | " + paciente.getNombre()
                + " | prioridad: " + prioridad + " | " + tipoAtencion
                + " | estado: " + atencion;
    }

    // Se guarda en una sola linea para mantener el estilo de persistencia manual del proyecto.
    public String aTextoJson() {

        return "{ \"id\": " + id
                + ", \"pacienteId\": " + paciente.getId()
                + ", \"nombre\": \"" + escapar(paciente.getNombre()) + "\""
                + ", \"identificacion\": \"" + escapar(paciente.getIdentificacion()) + "\""
                + ", \"edad\": " + paciente.getEdad()
                + ", \"tipoSeguro\": \"" + escapar(paciente.getTipoSeguro()) + "\""
                + ", \"fechaIngreso\": \"" + escapar(fechaIngreso) + "\""
                + ", \"fechaAtencion\": \"" + escapar(fechaAtencion) + "\""
                + ", \"prioridad\": \"" + prioridad + "\""
                + ", \"tipoAtencion\": \"" + tipoAtencion + "\""
                + ", \"atencion\": \"" + atencion + "\" }";
    }

    public static Tiquete desdeTexto(String linea) {

        Paciente paciente = new Paciente(
                Integer.parseInt(Json.valorDe(linea, "pacienteId")),
                Json.valorDe(linea, "nombre"),
                Json.valorDe(linea, "identificacion"),
                Integer.parseInt(Json.valorDe(linea, "edad")),
                Json.valorDe(linea, "tipoSeguro")
        );
        
        return new Tiquete(
                Integer.parseInt(Json.valorDe(linea, "id")),
                paciente,
                Json.valorDe(linea, "fechaIngreso"),
                Json.valorDe(linea, "fechaAtencion"),
                Json.valorDe(linea, "prioridad"),
                Json.valorDe(linea, "tipoAtencion"),
                Json.valorDe(linea, "atencion")
        );
    }

    private String escapar(String texto) {
        return texto == null ? "" : texto.replace("\"", "'");
    }
}
