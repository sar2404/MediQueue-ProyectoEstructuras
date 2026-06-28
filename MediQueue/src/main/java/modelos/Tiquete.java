package modelos;

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
        return "Tiquete{" + "id=" + id + ", paciente=" + paciente.getNombre() + ", prioridad='" + prioridad + '\'' + ", tipoAtencion='" + tipoAtencion + '\'' + '}';
    }

}
